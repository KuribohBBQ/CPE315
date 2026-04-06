# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt
.globl sumText

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program reverses the binary of a positive integer \n\n"

prompt:
	.asciiz " Enter integer 1: "

sumText: 
	.asciiz " \n Enter integer 2: "

#Text Area (i.e. instructions)
.text

main:
  # Display welcome message
	ori     $v0, $0, 4

	lui     $a0, 0x1001
	syscall