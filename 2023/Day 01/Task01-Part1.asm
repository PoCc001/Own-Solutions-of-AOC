section .data
FILE_NAME db "Input01.txt",0
PART1 db "Part 1: ",0
STRING_ARRAY dq -1

section .bss

section .note.GNU-stack     ; so that the linker (gcc) does not complain about the stack being executable or something

extern aoc_print
extern aoc_println
extern aoc_strlen
extern aoc_memcpy
extern aoc_memmove
extern aoc_int_to_decstr
extern aoc_uint_to_decstr
extern aoc_decstr_to_int
extern aoc_abort_msg
extern aoc_file_to_string_array
extern aoc_malloc
extern aoc_free
extern aoc_realloc
extern aoc_free_string
extern aoc_free_string_array
section .text
global main
main:
    push rbp
    mov rbp, rsp

    mov rdi, FILE_NAME
    call aoc_file_to_string_array
    mov [STRING_ARRAY], rax
    mov rdi, [rax]
    lea rdi, [rdi * 2 + rdi]
    shl rdi, 2
    call aoc_malloc
    mov r9, [STRING_ARRAY]
    mov rcx, 1
    xor esi, esi
    .digit_finding_loop:
        mov r8, [r9 + rcx * 8]
        xor edx, edx
        .inner_digit_finding_loop1:
            xor edi, edi
            mov dil, [r8 + rdx]
            cmp dil, 48
            js .after_digit1
            cmp dil, 58
            jns .after_digit1
            sub edi, 48
            imul edi, edi, 10
            mov [rax + rsi * 4], edi
            jmp .onto_the_second_inner_loop
            .after_digit1:
            inc rdx
            jmp .inner_digit_finding_loop1
        .onto_the_second_inner_loop:
        push rax
        push rcx
        push rdx
        push r8
        push r9
        push rdi
        push rsi
        mov rdi, r8
        call aoc_strlen
        pop rsi
        pop rdi
        pop r9
        pop r8
        pop rdx
        pop rcx
        lea rdx, [rax - 1]
        pop rax
        .inner_digit_finding_loop2:
            xor edi, edi
            mov dil, [r8 + rdx]
            cmp dil, 48
            js .after_digit2
            cmp dil, 58
            jns .after_digit2
            sub edi, 48
            add [rax + rsi * 4], edi
            jmp .end_inners
            .after_digit2:
            dec rdx
            jmp .inner_digit_finding_loop2
        .end_inners:
        inc rcx
        inc rsi
        lea rdx, [rcx - 1]
        cmp rdx, [r9]
        js .digit_finding_loop
    xor ecx, ecx
    xor edx, edx
    mov r8, [r9]
    .get_sum:
        mov edi, [rax + rcx * 4]
        add rdx, rdi
        inc rcx
        cmp rcx, r8
        js .get_sum
    push rdx
    mov rdi, PART1
    call aoc_print
    mov edi, 21
    call aoc_malloc
    pop rdi
    mov rsi, rax
    push rax
    call aoc_uint_to_decstr
    mov rdi, [rsp]
    call aoc_println
    pop rdi
    mov esi, 21
    call aoc_free
    mov rsp, rbp
    pop rbp
    ret
