#!/bin/sh
####################################
#
# See the other copy script for details
#
####################################

WORKING_DIR="../WORKING_DIR" # peter's working DIR is a relative address just one directory below the GIT repository
# The place where your project is...can be hard or relative
dest1="VERILOG_TOWN/verilogTown/src/com/me/myverilogTown/"
dest2="VERILOG_TOWN/verilogTown-desktop/src/com/me/myverilogTown/"
dest3="VERILOG_TOWN/verilogTown-android/assets/data/"

# What to backup. 
backup_files1="VERILOG_TOWN/verilogTown/src/com/me/myverilogTown/*.java"
backup_files2="VERILOG_TOWN/verilogTown-desktop/src/com/me/myverilogTown/*.java"
backup_files3="VERILOG_TOWN/verilogTown-android/assets/data/*.png"
backup_files4="VERILOG_TOWN/verilogTown-android/assets/data/*.mp3"
backup2_files1="$WORKING_DIR/verilogTown/src/com/me/myverilogTown/*.java"
backup2_files2="$WORKING_DIR/verilogTown-desktop/src/com/me/myverilogTown/*.java"
backup2_files3="$WORKING_DIR/verilogTown-android/assets/data/*.png"
backup2_files4="$WORKING_DIR/verilogTown-android/assets/data/*.mp3"

# Where to backup to.
back_dest="LOCAL_BACKUPS"

# Create archive filename.
day=$(date +%Y%m%d%H%M%S)
archive_file="copy_from_git_$day.tgz"
archive_file2="copy_from_working_$day.tgz"

# Print start status message.
echo "Backing up to $back_dest/$archive_file"
echo "Backing up to $back_dest/$archive_file2"
date
echo

# Backup the files using tar.
echo "tar -czf $back_dest/$archive_file $backup_files1 $backup_files2 $backup_files3 $backup_files4"
`tar -czf $back_dest/$archive_file $backup_files1 $backup_files2 $backup_files3 $backup_files4`
echo "tar -czf $back_dest/$archive_file2 $backup2_files1 $backup2_files2"
`tar -czf $back_dest/$archive_file2 $backup2_files1 $backup2_files2`

# Print end status message.
echo
echo "Backup finished"
date

# Long listing of files in $dest to check file sizes.
ls -lh $back_dest

# now copy to detination
echo "cp $backup2_files1 $dest1"
`cp $backup2_files1 $dest1`
echo "cp $backup2_files2 $dest2"
`cp $backup2_files2 $dest2`
echo "cp $backup2_files3 $dest3"
`cp $backup2_files3 $dest3`
echo "cp $backup2_files4 $dest3"
`cp $backup2_files4 $dest3`
