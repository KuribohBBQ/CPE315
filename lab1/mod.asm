# Name: Ryan Vu, Euclid Peregrin
# Section: 
# Description:

# ---------- Java ----------
# public class Mod {
#   public static int mod(int num, int div) {
#     return num & (div - 1);
#   }
  
#   public static void main(String[] args) {
#     System.out.println(mod(22, 4));
#   }
# }
# --------------------------

# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt
.globl sumText

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program performs fast mod \n\n"

prompt:
	.asciiz " Enter an integer: "

sumText: 
	.asciiz " \n Enter a divisor: "

#Text Area (i.e. instructions)
.text

main:
	# Display the welcome message (load 4 into $v0 to display)
	ori     $v0, $0, 4

	# This generates the starting address for the welcome message.
	# (assumes the register first contains 0).
	lui     $a0, 0x1001
	syscall

	# Ask for integer
	ori     $v0, $0, 4

	# This is the starting address of the prompt (notice the
	# different address from the welcome message)
	lui     $a0, 0x1001
	ori     $a0, $a0,0x23
	syscall

	# Read input
	ori		$v0, $0, 5
	syscall
	add		$t0, $0, $v0


	# Ask for divisor
	ori     $v0, $0, 4

	# This is the starting address of the prompt (notice the
	# different address from the welcome message)
	lui     $a0, 0x1001
	ori     $a0, $a0,0x37
	syscall

	# Read input
	ori		$v0, $0, 5
	syscall
	add		$t1, $0, $v0


	# Subtract 1 from divisor
	addi	$t1, $t1, -1

	# AND num and divisor
	and $t2, $t0, $t1

	ori		$v0, $0, 1
	add		$a0, $0, $t2
	syscall

	# Exit
	ori		$v0, $0, 10
	syscall
