#!/usr/bin/perl

use strict;
use warnings;

use File::Find;
use File::Path;
use File::Copy;
use File::Basename;
use Archive::Tar;
use Getopt::Long qw(:config no_ignore_case);

# This Perl script copies source codes and resources such as image files back
# and forth between the local project directory and the git repository.
# Currently this is just a quick-and-dirty approach and have only been tested
# within a test environment.
#
# Written by Naoki Mizuno

# TODO: Read config file

my $PACKAGE_NAME = "com.me.myverilogTown";

# "foobar" if you have "foobar-android" or "foobar-desktop" directories
my $NAME = "verilogTown";
my @postpositions = ( "", "-desktop", "-android", "-html" );
my @subdirectories = qw( src assets res );
my @extensions = qw( java png );
my $backup_dest = "$ENV{HOME}/verilogTownArchives";

my @files_to_copy;
my $backup_prefix_from;
my $backup_prefix_to;
my $help;

# Parse options
GetOptions(
    "backup-from|bf=s" => \$backup_prefix_from,
    "backup-to|bt=s" => \$backup_prefix_to,
    "dest|d=s" => \$backup_dest,
    "help|h" => \$help,
);

# Check for validity of arguments
if ($help or @ARGV < 2) {
    print_help();
    exit;
}

my $from = shift;
my $to = shift;

# Die if source directory can't be found
die "Can't find source directory $from" unless -d $from;

foreach my $postposition (@postpositions) {
    foreach my $subdirectory (@subdirectories) {
        # e.g. verilogTown-android/assets/
        my $dir_name = $NAME . $postposition . "/$subdirectory/";
        # e.g. verilogTown-android/src/com/me/myverilogTown
        $dir_name .= $PACKAGE_NAME =~ s#\.#/#gr if $subdirectory eq "src";

        next unless -d "$from/$dir_name";

        find(sub { add_files_to_copy($dir_name) }, "$from/$dir_name");
    }
}

copy_files();

# Create tars if specified
create_tars() if $backup_prefix_from or $backup_prefix_to;

sub add_files_to_copy {
    my $file_name = $_;
    my $dir_name = shift;

    # Return if the file name doesn't end with the extension
    return unless $file_name =~ m/\.@{[ join "|", @extensions ]}$/;

    # "Subtract" the "from" part
    # For example, if $File::Find::name is
    #   verilog_town/VERILOG_TOWN/verilogTown-html/src/com/me/myverilogTown/client/Foo.java
    # subtracting
    #   verilog_town/VERILOG_TOWN/
    # becomes
    #                             verilogTown-html/src/com/me/myverilogTown/client/Foo.java
    my $diff = $File::Find::name =~ s#$from##r;

    # Add to files that will be archived
    push @files_to_copy, $diff;
}

sub copy_files {
    for (my $i = 0; $i < scalar @files_to_copy; $i++) {
        # Note: Can't shift because this will be used later to create tar
        my $diff = $files_to_copy[$i];
        my $from_path = "$from/$diff";
        my $to_path = "$to/$diff";

        # Remove duplication of slashes
        trim_slashes(\$from_path);
        trim_slashes(\$to_path);

        # Create directory if it doesn't exist
        mkpath dirname $to_path unless -d dirname $to_path;

        if (copy $from_path, $to_path) {
            print "Copied $from_path\n\t=> $to_path\n";
        }
        else {
            die "Couldn't copy $from_path to $to_path: $!";
        }
    }
}

# Returns the path to the tar file in the format of foo/bar/baz/PREFIX_%Y%m%d%H%M%S.tar.gz
sub get_archive_file_path {
    my $dir = shift;
    my $file_name_prefix = shift;
    my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) = localtime();

    return sprintf "%s/%s_%4d%02d%02d%02d%02d%02d.tar.gz",
        $dir, $file_name_prefix, 1900 + $year, 1 + $mon, $mday, $hour, $min, $sec;
}

sub print_help {
    print <<EOF;
Usage: @{[basename $0]} [--backup-from|-bf PREFIX] [--backup-to|-bt PREFIX] [--dest|-d BACKUP_DESTINATION] [from dir] [to dir]

    --backup-from   -bf
            Add this option if you want to backup the source directory of
            where you're copying from. PREFIX is the prefix of the tar file
            name. For example, if you specify
                -bt FROM_LOCAL
            then the tar file will be named
                FROM_LOCAL_20140307171615.tar.gz
            where the numbers represent the date and time when the archive
            was created.

    --backup-to     -bt
            Same as --backup-from but use this to backup the source directory
            of the destination.

    --dest          -d
            The directory in which the tar file will be created. Defaults to
            \$HOME/verilogTownArchives if not specified.

    --help
            Show this help.


    The path can be either absolute or relative.
    Specify the the parent directory of the directories ending with
    "-desktop" or "-android" that were generated by the libgdx setup UI.
    For example, for the following directory tree,

    ...
    |
    +-- VERILOG_TOWN
    |       |
    |       +-- verilogTown
    |       |   |
    |       |   +-- src
    |       |   +-- ...
    |       +-- verilogTown-desktop
    |       |
    |       +-- verilogTown-android
    |       +-- ...
    +-- ...

    "VERILOG_TOWN" must be the argument.
EOF
}

# Create tar ball if option is specified
sub create_tars {
    my @tar_for_from;
    my @tar_for_to;
    foreach my $file (@files_to_copy) {
        push @tar_for_from, "$from/$file";
        push @tar_for_to, "$to/$file";
    }

    if ($backup_prefix_from) {
        my $tar = Archive::Tar->new();
        $tar->add_files(@tar_for_from);
        my $tar_file_path = get_archive_file_path($backup_dest, $backup_prefix_from);
        print "tar created as $tar_file_path\n" if $tar->write($tar_file_path, COMPRESS_GZIP);
    }
    if ($backup_prefix_to) {
        my $tar = Archive::Tar->new();
        $tar->add_files(@tar_for_to);
        my $tar_file_path = get_archive_file_path($backup_dest, $backup_prefix_to);
        print "tar created as $tar_file_path\n" if $tar->write($tar_file_path, COMPRESS_GZIP);
    }
}

sub trim_slashes {
    my $path_ref = shift;
    $$path_ref =~ s#/+#/#g;
}
