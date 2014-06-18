#!/usr/bin/perl

use strict;
use warnings;

# This Perl script is a temporary tool that will be used to add cars to the
# XML file. Be careful when reading this: it is **extremely** dirty code; I
# know I will hate myself for writing this if I had to read this a month from
# now...

my @cars;
my $prev_id;

print "Empty line to quit\n";
while (1) {
    print "[list|remove] (id)\n";
    print "(id) [start|end|delay] [value]: ";
    chomp(my $input = <STDIN>);

    # Exit if empty
    last if $input =~ /^$/;

    # list or remove
    if ($input =~ /^\s*l(ist|s)?/) {
        my $id = (split " ", $input)[1];
        if (defined $id) {
            show_car($id);
        }
        else {
            show_car($_) for 0..$#cars;
        }
        next;
    }
    elsif ($input =~ /^\s*r(emove|m)?/) {
        my $id = (split " ", $input)[1];
        if (defined $id) {
            $cars[$id] = undef;
            print "Removed car ID $id\n";
        }
        else {
            pop @cars;
            print "Removed last car\n";
        }
        next;
    }

    my %car;
    my ($id, $attr, $val);
    # Has ID
    if ($input =~ /^\s*\d/) {
        ($id, $attr, $val) = split " ", $input, 3;
    }
    else {
        # Use previous id if not specified
        if (defined $prev_id and not (
                defined $cars[$prev_id]{start} and
                defined $cars[$prev_id]{end} and
                defined $cars[$prev_id]{delay})) {
            $id = $prev_id;
        }
        else {
            $id = undef;
        }
        ($attr, $val) = split " ", $input, 2;
    }

    # Pull data if it exists
    %car = %{$cars[$id]} if defined $id and defined $cars[$id];

    if ($attr =~ /s(tart)?/) {
        my ($x, $y) = split /\s*[, ]\s*/, $val;
        $car{start} = [$x, $y];
    }
    elsif ($attr =~ /e(nd)?/) {
        my ($x, $y) = split /\s*[, ]\s*/, $val;
        $car{end} = [$x, $y];
    }
    elsif ($attr =~ /d(elay)?/) {
        $car{delay} = $val;
    }
    else {
        print <<EOF

Usage:
    [l|list] (ID)
        List the cars currently in stack.

    [r|rm|remove] (ID)
        Remove a car from stack. Removes the car with the largest ID if no ID is specified.

    (ID) [s|start] [x coord],[y coord]
        Adds the x and y coordinate as the starting point. Adds to the last of stack if no ID is specified.

    (ID) [e|end] [x coord],[y coord]
        Adds the x and y coordinate as the ending point. Adds to the last of stack if no ID is specified.

    (ID) [d|delay] [delay]
        Adds the delay. Adds to the last of stack if no ID is specified.


    When at least one of the three fields is not defined for the current car, the ID can be omitted.
    For example,

        s 0, 5
        e 2, 4
        e 3, 6
        d 300
        s 2, 8

    will set the starting point as (0, 5), ending point as (2, 4) but update to (3, 6), and sets the delay to 300.
    The last input sets the starting point (2, 8) for a new car, since all three fields has been filled for the previous car.

EOF
        ;
        next;
    }

    # Update data if exists, push if not
    if (defined $id) {
        $cars[$id] = \%car;
        print "Updated car ID $id\n";
        $prev_id = $id;
    }
    else {
        push @cars, \%car;
        print "Added car to stack\n";
        $prev_id = $#cars;
    }
}

# Print in XML format
open my $output_fh, ">", "output.xml" or die $!;
print $output_fh "<cars>\n";
for my $car (@cars) {
    next unless defined $car;

    print $output_fh "    <car>\n";
    print $output_fh "        <start x=\"$$car{start}[0]\" y=\"$$car{start}[1]\" />\n" if defined @$car{start};
    print $output_fh "        <end x=\"$$car{end}[0]\" y=\"$$car{end}[1]\" />\n" if defined @$car{end};
    print $output_fh "        <delay>$$car{delay}</delay>\n" if defined $$car{delay};
    print $output_fh "    </car>\n";
}
print $output_fh "</cars>\n";
close $output_fh;

# Print out car info
sub show_car {
    my $id = shift;

    return unless defined $cars[$id];

    my %car = %{$cars[$id]};

    printf "%2d\n", $id;
    printf "\tStart: %2d, %2d\n", @{$car{start}} if defined $car{start};
    printf "\tEnd:   %2d, %2d\n", @{$car{end}} if defined $car{end};
    printf "\tDelay: %4d\n", $car{delay} if defined $car{delay};
    print "\n";
}
