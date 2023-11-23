section .data
POWS_OF_TEN dq 10000000000000000000, 1000000000000000000, 100000000000000000, 10000000000000000, 1000000000000000, 100000000000000, 10000000000000, 1000000000000, 100000000000, 10000000000, 1000000000, 100000000, 10000000, 1000000, 100000, 10000, 1000, 100, 10
ERROR_MSG db "An error occured!",10,"Abort!",10,0

section .bss

section .note.GNU-stack         ; so that the linker (gcc) does not complain about the stack being executable or something

section .text
; These functions can be called from another assembly file so to say and are deemed ready for production (at least in AOC ;-))
global aoc_print, aoc_println, aoc_strlen, aoc_memcpy, aoc_memmove, aoc_int_to_decstr, aoc_uint_to_decstr, aoc_decstr_to_int, aoc_abort_msg, aoc_file_to_string_array, aoc_malloc, aoc_free, aoc_free_string, aoc_free_string_array


; INPUTS:
; rdi ... pointer to string
aoc_print:
    push rbp
    mov rbp, rsp
    push rdi
    call aoc_strlen
    pop rsi
    mov rdx, rax
    mov eax, 1
    mov edi, 1
    syscall
    mov rsp, rbp
    pop rbp
    ret

; INPUTS:
; rdi ... pointer to string
aoc_println:
    push rbp
    mov rbp, rsp
    call aoc_print
    sub rsp, 2
    mov word [rsp], 10
    mov rdi, rsp
    call aoc_print
    mov rsp, rbp
    pop rbp
    ret

; INPUTS:
; rdi ... pointer to string
; OUTPUTS:
; rax ... length (without '\0' character)
aoc_strlen:
    xor eax, eax
    cmp byte [rdi], 0
    jz .end
    .loop:
        add rax, 1
        cmp byte [rdi + rax], 0
        jnz .loop
    .end:
    ret

; INPUTS:
; rdi ... destination of the memory content
; rsi ... source of the memory content
; rcx ... length
aoc_memcpy:
    test rcx, rcx
    jz .end
    xor r8d, r8d
    mov rdx, rcx
    shr rdx, 3
    test rdx, rdx
    jz .loop2
    .loop1:
        mov r9, [rsi + 8 * r8]
        mov [rdi + 8 * r8], r9
        add r8, 1
        cmp rdx, r8
        jnz .loop1
    shl r8, 3
    .loop2:
        mov r9b, [rsi + r8]
        mov [rdi + r8], r9b
        add r8, 1
        cmp rcx, r8
        jnz .loop2
    .end:
    ret

; INPUTS:
; rdi ... destination of the memory content
; rsi ... source of the memory content
; rcx ... length
aoc_memcpy_backwards:
    test rcx, rcx
    jz .end
    mov rdx, rcx
    shr rdx, 3
    test rdx, rdx
    jz .preloop2
    mov edx, 8
    .loop1:
        sub rcx, rdx
        mov r9, [rsi + rcx]
        mov [rdi + rcx], r9
        cmp rcx, rdx
        jns .loop1
    jz .end
    .preloop2:
    sub rcx, 1
    .loop2:
        mov r9b, [rsi + rcx]
        mov [rdi + rcx], r9b
        sub rcx, 1
        test rcx, rcx
        jns .loop2
    .end:
    ret

; INPUTS:
; rdi ... destination of the memory content
; rsi ... source of the memory content
; rcx ... length
aoc_memmove:
    push rbp
    mov rbp, rsp
    cmp rdi, rsi
    jz .end
    js .forward
    call aoc_memcpy_backwards
    .forward:
    call aoc_memcpy
    .end:
    mov rsp, rbp
    pop rbp
    ret

; INPUTS:
; rdi ... integer value (64 bit)
; rsi ... pointer to memory allocated for the string (needs to be large enough)
aoc_int_to_decstr:
    test rdi, rdi
    jnz .notzero
    mov word [rsi], 48
    mov word [rsi + 1], 0
    jmp .end
    .notzero:
    jns .notnegative
    mov byte [rsi], 45
    add rsi, 1
    neg rdi
    .notnegative:
    cmp rdi, 10
    jns .normal
    lea ecx, [rdi + 48]
    mov [rsi], cx
    mov byte [rsi + 1], 0
    jmp .end
    .normal:
    xor ecx, ecx
    xor r8d, r8d
    mov rax, rdi
    cmp rdi, [POWS_OF_TEN]
    jnc .loop
    jz .loop
    .preloop:
        add ecx, 1
        cmp rdi, [POWS_OF_TEN + 8 * rcx]
        js .preloop
    .loop:
        xor edx, edx
        div qword [POWS_OF_TEN + 8 * rcx]
        lea r9d, [rax + 48]        
        mov [rsi + r8], r9b
        mov rax, rdx
        add ecx, 1
        add r8d, 1
        cmp ecx, 19
        js .loop
    lea r9d, [rax + 48]
    mov [rsi + r8], r9b
    mov byte [rsi + r8 + 1], 0
    .end:
    ret

; INPUTS:
; rdi ... unsigned integer value (64 bit)
; rsi ... pointer to memory allocated for the string (needs to be large enough)
aoc_uint_to_decstr:
    test rdi, rdi
    jnz .notzero
    mov word [rsi], 48
    mov word [rsi + 1], 0
    jmp .end
    .notzero:
    cmp rdi, 10
    jnc .normal
    lea ecx, [rdi + 48]
    mov [rsi], cx
    mov byte [rsi + 1], 0
    jmp .end
    .normal:
    xor ecx, ecx
    xor r8d, r8d
    mov rax, rdi
    cmp rdi, [POWS_OF_TEN]
    jnc .loop
    jz .loop
    .preloop:
        add ecx, 1
        cmp rdi, [POWS_OF_TEN + 8 * rcx]
        js .preloop
    .loop:
        xor edx, edx
        div qword [POWS_OF_TEN + 8 * rcx]
        lea r9d, [rax + 48]        
        mov [rsi + r8], r9b
        mov rax, rdx
        add ecx, 1
        add r8d, 1
        cmp ecx, 19
        js .loop
    lea r9d, [rax + 48]
    mov [rsi + r8], r9b
    mov byte [rsi + r8 + 1], 0
    .end:
    ret

; INPUTS:
; rdi ... base
; rsi ... exponent
; rcx ... modulus
; OUTPUTS:
; rax ... (base^exponent mod modulus)
aoc_unsigned_powermod:
    mov r8d, 1
    xor edx, edx
    xor r9d, r9d
    mov rax, rdi
    div rcx
    mov rdi, rdx
    .loop:
        bt rsi, r9
        jnc .zero
        mov rax, r8
        mul rdi
        div rcx
        mov r8, rdx
        .zero:
        mov rax, rdi
        mul rax
        div rcx
        mov rdi, rdx
        add r9d, 1
        cmp r9d, 64
        jnz .loop
    mov rax, r8
    ret

%define RABIN_KARP_PRIME 0xfffffffb

; INPUTS:
; rdi ... pointer to string
; rsi ... length of the string
; rcx ... number for modulo calculation (ideally a prime number that is as large as possible)
; OUTPUTS:
; rax ... hash value
aoc_rabin_karp_hash_full:
    lea rdx, [rsi + 0xffffffffffffffff]
    cmp rsi, 9
    jnc .complex
    mov r9, rcx
    xor ecx, ecx
    xor eax, eax
    .loop:
        xor r8d, r8d
        mov r8b, [rdi + rdx]
        shl r8, cl
        or rax, r8
        add ecx, 8
        sub rdx, 1
        test edx, edx
        jns .loop
    xor edx, edx
    div r9
    mov eax, edx
    jmp .end
    .complex:
    mov r8d, 256
    mov r11d, 1
    mov eax, 1
    xor r9d, r9d
    xor r10d, r10d
    .loop_complex:
        sub rsi, 1
        mov r9b, [rdi + rsi]
        mov rax, r11
        mul r8
        xor edx, edx
        div rcx
        mov r11, rdx
        mov rax, rdx
        mul r9
        xor edx, edx
        div rcx
        add r10, rdx
        xor edx, edx
        div rcx
        mov r10d, edx
        test rsi, rsi
        jnz .loop_complex
    mov eax, r10d
    .end:
    ret

; INPUTS:
; rdi ... pointer to string
; rsi ... length of string
; rcx ... number for modulo calculation (ideally a prime number that is as large as possible)
; rdx ... previous hash value
; OUTPUTS:
; rax ... new hash value
aoc_rabin_karp_hash_rolling:
    push rbp
    mov rbp, rsp

    shl rdx, 8
    push rdi
    push rsi
    push rcx
    push rdx
    mov edi, 256
    call aoc_unsigned_powermod
    pop rdx
    pop rcx
    pop rsi
    pop rdi
    mul qword [rdi + 0xffffffffffffffff]
    sub rdx, rax
    add rdx, [rdi + rsi + 0xffffffffffffffff]
    mov rax, rdx
    div rcx

    mov rsp, rbp
    pop rbp
    ret

; INPUTS:
; rdi ... pointer to string
; rsi ... length of string
; rcx ... number for modulo calculation (ideally a prime number that is as large as possible)
; rdx ... previous hash value (or < 0 (64 bit signed), if not present)
; OUTPUTS:
; rax ... current hash value
aoc_rabin_karp_hash:
    push rbp
    mov rbp, rsp

    cmp rdx, 0
    js .full_hash
    call aoc_rabin_karp_hash_rolling
    jmp .end
    .full_hash:
    call aoc_rabin_karp_hash_full
    .end:

    mov rsp, rbp
    pop rbp
    ret

; INPUTS:
; rdi ... string, in which the pattern should be found
; rsi ... pattern string
; OUTPUTS:
; rax ... index of the first occurrence of the pattern from the first character on (or -1 if the pattern could not be found)
aoc_find_first_in_str:
    push rbp
    mov rbp, rsp
    push r15
    push r14
    push rdi
    push rsi

    call aoc_strlen
    push rax
    mov rdi, [rsp + 8]
    call aoc_strlen
    cmp qword [rsp], 100
    jc .rabin_karp
    cmp rax, 10
    jc .rabin_karp

    
    jmp .end
    .rabin_karp:
    sub [rsp], rax
    mov rdi, [rsp + 8]
    mov rsi, [rsp]
    mov rcx, RABIN_KARP_PRIME
    mov r14, rax
    call aoc_rabin_karp_hash_full
    mov r15, rax
    mov rdi, [rsp + 16]
    mov rsi, r14
    mov rcx, RABIN_KARP_PRIME
    call aoc_rabin_karp_hash_full
    xor ecx, ecx
    .loop:
        cmp rax, r15
        jnz .no_match
        xor edx, edx
        mov r11, rcx
        .innerloop:
            mov r9, [rsp + 16]
            mov r10, [rsp + 8]
            mov r8b, [r9 + rdx]
            add rdx, 1
            cmp r8b, [r10 + r11]
            lea r11, [rcx + rdx]
            jnz .no_match
            cmp r11, [rsp]
            jnz .innerloop
            cmp rdx, r14
            jnz .innerloop
        mov rax, rcx
        jmp .end
        .no_match:
        add rcx, 1
        mov rdi, [rsp + 16]
        add rdi, rcx
        mov rsi, r14
        push rcx
        mov rcx, RABIN_KARP_PRIME
        mov rdx, rax
        call aoc_rabin_karp_hash_rolling
        pop rcx
        
    .end:
    pop rdx
    pop rsi
    pop rdi
    pop r14
    pop r15
    mov rsp, rbp
    pop rbp
    ret

; INPUTS:
; rdi ... pointer to string
; OUTPUTS:
; rax ... unsigned int value
aoc_decstr_to_int:
    push rbp
    mov rbp, rsp
    push rdi
    call aoc_strlen
    xor r11d, r11d
    test rax, rax
    jz .end
    pop rdi
    push r15
    push r14
    mov r15, rax
    mov r8d, 10
    mov rax, 1
    xor r10d, r10d
    .loop:
        sub r15, 1
        mov r10b, [rdi + r15]
        sub r10d, 48
        js .maybe_sign
        cmp r10d, 10
        jns .end
        mov r14, rax
        mov eax, r10d
        mul r14
        test edx, edx
        jnz .end
        add r11, rax
        mov rax, r14
        mul r8
        test edx, edx
        jnz .end
        test r15, r15
        jnz .loop
    .maybe_sign:
    mov r8, r11
    neg r8
    cmp r10d, 0xfffffffd
    cmovz r11, r8
    .end:
    mov rax, r11
    pop r14
    pop r15
    mov rsp, rbp
    pop rbp
    ret

; INPUTS:
; rdi ... pointer to array of elements
; rsi ... size of one element in bytes
; rcx ... number of elements
; rdx ... pointer to element to be found
; OUTPUTS:
; rax ... index of found value
aoc_find_first_unsorted:
    mov rax, 0xffffffffffffffff
    test rcx, rcx
    jz .end
    mov r10, rdi

    .loop:
        xor r8d, r8d
        .loop_inner:
            add r8, 1
            mov r9b, [rdi]
            add rdi, 1
            cmp r8, rsi
            jnz .loop
            cmp r9b, [rdx + r8 + 0xffffffffffffffff]
            jz .loop_inner
            sub rdi, r10
            mov rax, rdi

    .end:
    ret

aoc_abort_msg:
    mov rdi, ERROR_MSG
    call aoc_print
    mov eax, 60
    mov edi, 1
    syscall

; INPUTS:
; rdi ... pointer to the name of the file (whole path)
; OUTPUTS:
; rax ... pointer to the string array from the file (if there is an error, the program aborts with a small error message)
aoc_file_to_string_array:
    push rbp
    mov rbp, rsp
    mov eax, 2
    xor esi, esi
    syscall
    test eax, eax
    jns .opened_file
    call aoc_abort_msg
    .opened_file:
    push rax
    mov edi, eax
    xor esi, esi
    mov edx, 2
    mov eax, 8
    syscall
    inc rax
    test rax, rax
    jnz .got_length
    call aoc_abort_msg
    .got_length:
    push rax
    mov edi, [rsp + 8]
    xor esi, esi
    xor edx, edx
    mov eax, 8
    syscall
    cmp rax, -1
    jnz .good_set
    call aoc_abort_msg
    .good_set:
    pop rsi
    xor edi, edi
    mov edx, 3
    mov r10d, 2
    mov r8, [rsp]
    push rsi
    xor r9d, r9d
    mov eax, 9
    syscall
    cmp rax, -1
    jnz .allocated_str
    call aoc_abort_msg
    .allocated_str:
    mov rcx, [rsp]
    dec rcx
    mov byte [rax + rcx], 0
    mov rdi, [rsp + 8]
    push rax
    mov eax, 3
    syscall
    pop rax
    xor esi, esi
    mov rdx, -1
    xor r8d, r8d
    .counting_loop:
        inc rdx
        mov r8b, [rax + rdx]
        sub r8d, 11
        shr r8d, 31
        add esi, r8d
        cmp byte [rax + rdx], 0
        jnz .counting_loop
    lea rsi, [rsi * 8 + 8]
    push r14
    mov r14, rax
    xor edi, edi
    xor r9d, r9d
    push rsi
    mov edx, 3
    mov r10d, 34
    mov r8, -1
    mov eax, 9
    syscall
    cmp rax, -1
    jnz .allocated_arr
    call aoc_abort_msg
    .allocated_arr:
    pop rsi
    shr rsi, 3
    sub rsi, 2
    mov [rax], rsi
    push r12
    push r13
    push r15
    xor r12d, r12d
    xor r13d, r13d
    mov r15, rax
    .allocation_and_copy_loop:
        xor ecx, ecx
        xor r8d, r8d
        .inner_loop:
            mov r8b, [r14 + r12]
            inc r12
            inc rcx
            cmp r8d, 11
            jns .inner_loop
        sub r12, rcx
        mov eax, 9
        mov edx, 3
        mov r10d, 34
        mov r8, -1
        xor edi, edi
        xor r9d, r9d
        mov rsi, rcx
        syscall
        cmp rax, -1
        jnz .good_string
        call aoc_abort_msg
        .good_string:
        xor r8d, r8d
        mov [r15 + r13 * 8 + 8], rax
        inc r13
        xor ecx, ecx
        cmp rax, -1
        jnz .inner_loop2
        call aoc_abort_msg
        .inner_loop2:
            mov r8b, [r14 + r12]
            inc r12
            inc rcx
            test r8d, r8d
            jz .end
            cmp r8d, 11
            js .allocation_and_copy_loop
            mov [rax + rcx - 1], r8b
            jmp .inner_loop2

    .end:
    mov rdi, r14
    mov rsi, [rsp + 32]
    mov eax, 11
    syscall
    mov rax, r15
    pop r15
    pop r13
    pop r12
    pop r14
    add rsp, 16
    mov rsp, rbp
    pop rbp
    ret

; INPUTS:
; rdi ... number of bytes to allocate on the heap
; OUTPUTS:
; rax ... pointer to allocated memory (-1 if unsuccessful)
aoc_malloc:
    push rbp
    mov rbp, rsp
    mov rsi, rdi
    xor edi, edi
    mov edx, 3
    mov r10d, 34
    mov r8, -1
    mov eax, 9
    syscall
    mov rsp, rbp
    pop rbp
    ret

; INPUTS:
; rdi ... pointer to memory on the heap that needs to be freed
; rsi ... length of memory to deallocate in bytes
; OUTPUTS:
; rax ... 0 for success; -1 for failure
aoc_free:
    push rbp
    mov rbp, rsp

    mov eax, 11
    syscall

    mov rsp, rbp
    pop rbp
    ret

; INPUTS:
; rdi ... pointer to the string on the heap that needs to be freed
; OUTPUTS:
; rax ... 0 for success; -1 for failure
aoc_free_string:
    push rbp
    mov rbp, rsp
    push rdi
    call aoc_strlen
    pop rdi
    lea rsi, [rax + 1]
    mov eax, 11
    syscall
    mov rsp, rbp
    pop rbp
    ret

; INPUTS:
; rdi ... pointer to the string array
aoc_free_string_array:
    push rbp
    mov rbp, rsp
    push r15
    push r14
    push r13
    mov r15, [rdi]
    lea r14, [rdi + 8]
    xor r13d, r13d
    .free_loop:
        cmp r13, r15
        jz .end
        mov rdi, [r14 + r13]
        call aoc_free_string
        cmp rax, -1
        jnz .good_free
        call aoc_abort_msg
        .good_free:
        inc r13
        jmp .free_loop

    .end:
    lea rdi, [r14 - 8]
    lea rsi, [r15 * 8 + 8]
    call aoc_free
    cmp rax, -1
    jnz .good_final_free
    call aoc_abort_msg
    .good_final_free:
    pop r13
    pop r14
    pop r15
    mov rsp, rbp
    pop rbp
    ret
