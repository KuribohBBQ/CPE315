# declare global so programmer can see actual addresses.
.globl welcome
.globl promptOne
.globl promptTwo

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program reverses the binary of a positive integer \n\n"

promptOne:
	.asciiz " Enter integer 1: "

promptTwo: 
	.asciiz " \n Enter integer 2: "

#Text Area (i.e. instructions)
.text

main:
  # Display welcome message
	ori     $v0, $0, 4

	lui     $a0, 0x1001
	syscall

  # Display promptOne
  ori     $v0, $0, 4

	lui     $a0, 0x1001
  ori     $a0, 0x3b
	syscall

  


  # Display promptTwo
  ori     $v0, $0, 4

	lui     $a0, 0x1001
  ori     $a0, 0x50
	syscall