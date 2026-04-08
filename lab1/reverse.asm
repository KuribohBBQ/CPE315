# declare global so programmer can see actual addresses.
.globl welcome
.globl promptOne

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program reverses the binary of a positive integer \n\n"

promptOne:
	.asciiz " Enter a positive integer: "

newLine:
	.asciiz "\n"

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

  # Read input into $t0
	ori		  $v0, $0, 5
	syscall
	add		  $t0, $0, $v0

  # $t1 is the counter variable
  add     $t1, $0, $0

  # Loop to construct reversed number
  loop_reverse:
    slti    $t2, $t1, 32    # if $t1 >= 32 set $t2 = 0
    beq     $t2, $0, end

    # construct reversed bit
    andi    $t3, $t0, 1     # extract the leasst significant bit
    sll     $t4, $t4, 1     # left shift result by one; $t4 holds result
    or      $t4, $t4, $t3   # sets the extracted bit into the result
    srl     $t0, $t0, 1     # right shift number by one

    addi    $t1, $t1, 1     # increment counter
    j       loop_reverse
  
  end:
    # Print reversed number
    ori		$v0, $0, 1
    add		$a0, $0, $t4
    syscall

    # Exit
    ori		$v0, $0, 10
    syscall
    