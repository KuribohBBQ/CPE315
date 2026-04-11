# Name: Ryan Vu, Euclid Peregrin
# Section:
# Description: Divides a 64-bit unsigned number by a 31-bit number that is always a power of two. First takes in a high input, then a low input, then finally
# the divisor. Divisor is shifted to the right. LSB of high is extracted, then high and low are shifted to the right, then the extracted bit is OR with the MSB of low. This repeats until
# divisor reaches 0.
#   CPE 315

#Java 
# int high = scanner.nextInt();
# int low = scanner.nextInt();
# int divisor = scanner.nextInt();
#
# while (true) {
#     divisor = divisor >>> 1;
#     if (divisor == 0) break;
#
#     int bit = high & 1;
#     high = high >>> 1;
#     low = low >>> 1;
#     bit = bit << 31;
#     low = low | bit;
# }
#
# System.out.println(high + "," + low);




# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt_high
.globl prompt_low
.globl divisorText
.globl comma

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program divides a number \n\n"

prompt_high:
	.asciiz " Enter a high: "

prompt_low:
    .asciiz " \n Enter a low: "

comma:
    .asciiz ","

divisorText:
	.asciiz " \n Enter a divisor "

#Text Area (i.e. instructions)
.text

main:

	# Display the welcome message (load 4 into $v0 to display)
	ori     $v0, $0, 4
	la      $a0, welcome
	syscall

	#ask for high
	ori     $v0, $0, 4
	la      $a0, prompt_high
	syscall

	#read high input
	ori     $v0, $0, 5
	syscall
    #put high input into a register
	add     $t0, $v0, $0

	#ask for low
    ori     $v0, $0, 4
    la      $a0, prompt_low
    syscall

    #read low input
    ori     $v0, $0, 5
    syscall
    #put low input into a register
    add     $t1, $v0, $0

    #ask for divisor
    ori     $v0, $0, 4
    la      $a0, divisorText
    syscall

    #read divisor input
    ori     $v0, $0, 5
    syscall
    #put divisor into a register
    add     $t2, $v0, $0

    loop_shifting:
    srl     $t2, $t2, 1     #shift divisor to the right
    beq     $t2, $0, done   #if divisor = 0, jump to done branch
    andi    $t3, $t0, 1     #extract LSB from high and store into t3
    srl     $t0, $t0, 1     #shift high to the right
    srl     $t1, $t1, 1     #shift low to the right

    sll     $t3, $t3, 31    #shift extracted bit to be MSB in its register

    or     $t1, $t1, $t3    #add extracted bit to MSB of low
    j       loop_shifting

    done:
    #print high
    ori $v0, $0, 1
    add $a0, $t0, $0
    syscall

    #comma
    ori $v0, $0, 4
    la  $a0, comma
    syscall


    #print low
    ori $v0, $0, 1
    add $a0, $t1, $0
    syscall

    #exit program
    ori $v0, $0, 10
    syscall
