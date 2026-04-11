# Name: Ryan Vu, Euclid Peregrin
# Section: 
# Description: 

# ---------- Java ----------
# public class Exponent {
#   public static int exponent(int x, int y) {

#     if (y == 1) {
#       return x;
#     }

#     int res = x;
#     int current = 0;

#     while (y > 1) {
#       for (int i = x; i > 1; i--) {
#         current += res;
#       }
#       res += current;
#       current = 0;
#       y--;
#     }

#     return res;
#   }

#   public static void main(String[] args) {
#     System.out.println(exponent(15, 7));
#   }
# }
# --------------------------

# declare global so programmer can see actual addresses.
.globl welcome
.globl promptOne

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program performs exponentiation \n\n"

promptBase:
	.asciiz " Enter a base: "

promptExponent:
	.asciiz "\n Enter an exponent: "

newLine:
	.asciiz "\n"

#Text Area (i.e. instructions)
.text

main:
  # Display welcome message
	ori     $v0, $0, 4
	lui     $a0, 0x1001
	syscall

  # Display promptBase
  ori     $v0, $0, 4
	la      $a0, promptBase
	syscall

  # Read base into $t0
	ori		  $v0, $0, 5
	syscall
	add		  $t0, $0, $v0

  # Display promptExponent
  ori     $v0, $0, 4
	la      $a0, promptExponent
	syscall

  # Read exponent into $t1
	ori		  $v0, $0, 5
	syscall
	add		  $t1, $0, $v0

  # Power of 1 case
  addi    $t2, $0, 1
  add     $t3, $t0, $0
  beq     $t1, $t2, done
  
  add     $t3, $0, $0
  add     $t3, $t3, $t0
  add     $t4, $t4, $0

  while_loop:
    add     $t5, $t5, $t0     # set counter = x (base)

  for_loop:
    add     $t4, $t4, $t3     # do one addition
    addi    $t5, $t5, -1      # decrement counter
    bgtz    $t5, for_loop     # loop again unless counter <= 0

  for_done:
    add     $t3, $0, $t4          # update result
    add     $t4, $0, $0           # reset $t4
    addi    $t1, $t1, -1          # decrement exponent y
    bgt     $t1, $t2, while_loop  # while loop again unless exponent <= 1

  done:
    # Print result
    ori     $v0, $0, 1
    add     $a0, $t3, $0
    syscall

    # Exit
    ori		  $v0, $0, 10
    syscall
    