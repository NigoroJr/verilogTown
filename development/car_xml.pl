#!/usr/bin/perl

use strict;
use warnings;

use 5.010;

use XML::Simple;

# Constants
my $LIST_CARS = "list cars";
my $LIST_START_END = "list start end";
my $ADD = "add";
my $REMOVE = "remove";
my $QUIT = "quit";
my $HELP = "help";

my $level_xml = shift;
# Exit program if no file specified
exit 1 if not defined $level_xml and help();

my $level_ref = XMLin($level_xml);
populate_cars();

my @cars;
my @starts;
my @ends;

parse_start_end();

while (1) {
    my ($action, $id, $start, $end, $delay) = get_input();

    given ($action) {
        last when $action eq $QUIT;
        add_car($id, $start, $end, $delay) when $action eq $ADD;
        remove_car($id) when $action eq $REMOVE;
        list_cars() when $action eq $LIST_CARS;
        list_start_end() when $action eq $LIST_START_END;
        default { help(); };
    }
}

# Print in XML format
print_to_XML($level_xml);

sub populate_cars {
    my @orig_cars = @{$level_ref->{cars}->{car}};

    foreach my $orig_car (@orig_cars) {
        my %car = (
            'start' => [$$orig_car{start}],
            'end'   => [$$orig_car{end}],
            'delay' => [$$orig_car{delay}],
        );
        push @cars, \%car;
    }
}

sub get_input {
    print "Enter data or command: ";
    chomp(my $input = <STDIN>);

    given ($input) {
        return $QUIT when /^$|q(uit)?/;
        return ($REMOVE, $1) when /r(?:emove|m)?\s*(\d+)/i;
        return $LIST_START_END when /l(ist|s)?\s*se/i;
        return $LIST_CARS when /l(ist|s)?(?!\s*se)/i;

        when (/
            (?<id>\d+)?     # Optional
            \s*
            (?<start>\d+)
            \s+
            (?<end>\d+)
            \s+
            (?<delay>\d+)
            /x) {
            my $id = (defined $+{id} ? $+{id} : -1);
            return ($ADD, $id, $+{start}, $+{end}, $+{delay});
        }

        default { return $HELP; };
    }
}

sub add_car {
    my ($car_id, $start_id, $end_id, $delay) = @_;
    my %car = (
        'start' => [$starts[$start_id]],
        'end'   => [$ends[$end_id]],
        'delay' => [$delay],
    );

    # ID -1 means no car is to be updated
    if ($car_id == -1) {
        push @cars, \%car;
        print "Added a car to stack\n";
    }
    # Update specified car if ID is given
    else {
        $cars[$car_id] = \%car;
        print "Update Car $car_id\n";
    }
}

sub remove_car {
    my $id = shift;

    $cars[$id] = undef if defined $id;
}

sub list_cars {
    print "No cars yet!\n" unless @cars;
    print_car($_) for 0..$#cars;
}

sub list_start_end {
    print "Starting points:\n";
    print "\t$_: $starts[$_]->{x}, $starts[$_]->{y}\n" for 0..$#starts;
    print "Ending points:\n";
    print "\t$_: $ends[$_]->{x}, $ends[$_]->{y}\n" for 0..$#ends;
}

sub print_car {
    my $id = shift;
    my $car_hash_ref = $cars[$id];

    return unless defined $car_hash_ref;

    my %car = %$car_hash_ref;
    print "Car $id\n";
    print "\tStart: $car{start}[0]{x}, $car{start}[0]{y}\n";
    print "\tEnd:   $car{end}[0]{x}, $car{end}[0]{y}\n";
    print "\tDelay: $car{delay}[0]\n";
}

sub parse_start_end {   # {{{
    my @grids = @{$level_ref->{map}->{grid}};

    foreach my $grid (@grids) {
        next unless $$grid{type} =~ /^(START|END)/;

        push @starts, {
            "x" => $$grid{x},
            "y" => $$grid{y},
        } if $$grid{type} =~ /^START/;
        push @ends, {
            "x" => $$grid{x},
            "y" => $$grid{y},
        } if $$grid{type} =~ /^END/;
    }
}   # }}}

sub print_to_XML {
    my $filename = shift;
    correct_grids();

    $$level_ref{cars}{car} = \@cars;
    # print XMLout($level_ref, 'RootName' => 'level');
    open my $xml_fh, ">", $filename or die $!;
    print $xml_fh XMLout($level_ref, 'RootName' => 'level');

    close $xml_fh;
}

# The XML::Simple module doesn't make the <type> an array reference
sub correct_grids {
    my @grids = @{$$level_ref{map}{grid}};

    foreach my $grid (@grids) {
        $$grid{type} = [$$grid{type}];
        $$grid{intersection} = [$$grid{intersection}] if defined $$grid{intersection};
    }

    $$level_ref{map}{grid} = \@grids;
}

sub help {  # {{{
    print "Empty line to quit\n";
    print <<EOF

Usage: $0 <XML file>

    If the XML file doesn't exist, it will be created after the car
    information has been input.

Commands:
    // TODO

EOF
    ;
}   # }}}
