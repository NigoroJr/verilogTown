#!/bin/sh
####################################
#
# Move GIT files to your project
#
####################################

# The place where your project is...can be hard or relative
dest1="VERILOG_TOWN/verilogTown/src/com/me/myverilogTown/"
dest2="VERILOG_TOWN/verilogTown-desktop/src/com/me/myverilogTown/"
dest3="VERILOG_TOWN/verilogTown-android/assets/data/"

# What to backup. 
backup_files1="../WORKING_DIR/verilogTown/src/com/me/myverilogTown/*.java"
backup_files2="../WORKING_DIR/verilogTown-desktop/src/com/me/myverilogTown/*.java"
backup_files3="../WORKING_DIR/verilogTown-android/assets/data/*.png"

# Where to backup to.
back_dest="LOCAL_BACKUPS"

# Create archive filename.
day=$(date +%Y%m%d%H%M%S)
archive_file="copy_from_working_to_git_$day.tgz"

# Print start status message.
echo "Backing up to $back_dest/$archive_file"
date
echo

# Backup the files using tar.
echo "tar -czf $back_dest/$archive_file $backup_files1 $backup_files2 $backup_files3"
`tar -czf $back_dest/$archive_file $backup_files1 $backup_files2 $backup_files3`

# Print end status message.
echo
echo "Backup finished"
date

# Long listing of files in $dest to check file sizes.
ls -lh $back_dest

# now copy to detination
echo "cp $backup_files1 $dest1"
`cp $backup_files1 $dest1`
echo "cp $backup_files2 $dest2"
`cp $backup_files2 $dest2`
echo "cp $backup_files3 $dest3"
`cp $backup_files3 $dest3`
