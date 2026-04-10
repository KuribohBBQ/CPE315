# Name: Ryan Vu, Euclid Peregrin
# Section: 
# Description: 


# declare global so programmer can see actual addresses.
.globl welcome
.globl promptOne

# Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program divides a 64-bit number with a 32-bit number \n\n"

promptDividendOne:
	.asciiz " Enter dividend high 32: "

promptDividendTwo:
	.asciiz "\n Enter dividend low 32: "

promptDivisor:
  .asciiz "\n Enter divisor: "

newLine:
	.asciiz "\n"

# Text Area (i.e. instructions)
.text

main:
  # Display welcome message
	ori     $v0, $0, 4
	lui     $a0, 0x1001
	syscall

  # Display promptDividendOne
  ori     $v0, $0, 4
	lui     $a0, 0x1001
  ori     $a0, 0x3e
	syscall

  # Read input into $t0
	ori		  $v0, $0, 5
	syscall
	add		  $t0, $0, $v0

  # Display promptDividendTwo
  ori     $v0, $0, 4
	lui     $a0, 0x1001
  ori     $a0, 0x58
	syscall

  # Read input into $t1
	ori		  $v0, $0, 5
	syscall
	add		  $t1, $0, $v0

 # Display promptDivisor
  ori     $v0, $0, 4
	lui     $a0, 0x1001
  ori     $a0, 0x72
	syscall

  # Read input into $t2
	ori		  $v0, $0, 5
	syscall
	add		  $t2, $0, $v0

  
  
  
  
  
  
  
  
  
  
  
  
  
  # Exit
  ori		$v0, $0, 10
  syscall
    