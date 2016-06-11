
; You may customize this and other start-up templates; 
; The location of this template is c:\emu8086\inc\0_com_template.txt

.model small
 .stack 100h

 .data
    ;origArray dw 0,1,2,3,4, 5,6,7,8,9, 10,11,12,13,14, 15,16,17,18,19;, 20,21,22,23,24
    origArray dw 3,9,8,3,7, 5,9,7,8,1, 8,9,1,6,9, 5,7,3,1,2
    newArray dw 100 dup (0)
    
    divisor db 4    
 
    msgRows db "Enter the number of rows: $"
    msgCols db "Enter the number of columns: $"
    msgIters db "Enter the number of iterations: $" 
    msgOrigMat db "Original Matrix : ",10,13,"$"
    msgNewMat db "New Matrix : ",10,13,"$"
    msgRelMat db "Relaxed Matrix : ",10,13,"$"
    msgIter db "Iteration Number: ",10,13,"$"
            
	rows dw 4		;Added from Filmification v1.0
	cols dw 4		;Added from Filmification v1.0
    sizeArray dw 20
    sizeLimit dw 15    
	itersLimit dw 1		;Added from Filmification v1.0
    
    ;iters dw 0
    ;colsLimit dw 3
            
    ;rows dw 0    
    ;cols dw 0            
    ;sizeArray dw 0     
    ;sizeLimit dw 0       
    ;iters dw 0
    ;itersLimit dw 0 
    
    iters dw 0
    colsLimit dw 0    
    
    input  db 30 dup ('$') 
    output db 30 dup ('$')
    count  dw ?
               
 .code
    main proc
        mov ax, @data                   ; initialize data segment
        mov ds, ax
        
         @      
        ;mov ah, 09			    
        ;mov dx, offset msgRows	
        ;int 21h
        
        ;call getInput
        ;mov rows, bp
        
        ;call newline
         
        ;mov ah, 09			    
        ;mov dx, offset msgCols	
        ;int 21h
         
        ;call getInput
        ;mov cols, bp
        
        push ax
        mov ax, cols
        sub ax, 2
        mov colsLimit, ax 
        pop ax
        
        push ax        
        mov ax, rows
        mov bx, cols
        mul bl
        mov sizeArray, ax
        sub ax, cols
        mov sizeLimit, ax        
        pop ax
        
        ;call newline
         
        ;mov ah, 09			    
        ;mov dx, offset msgIters	
        ;int 21h
         
        ;call getInput
        ;mov itersLimit, bp
        @ 
        
        ;call newline 
        ;call printOrigArray
          
        ;call printNewArray
        
        call copyToNewArray
        
        ;call printNewArray
         
        call jacobi
        
        
         
        mov ah, 4ch                     ; return control to DOS
        int 21h
    endp main  
         
    proc jacobi
        push ax
        push bx
        push cx
        push dx
        
        mov count, 1
        mov cx, cols
        add cx, 1
        
        ifValid:
            push ax
            mov ax, sizeLimit
            cmp ax, cx 
            pop ax
            jg relax
            jle done  
        
        relax:
            ;call newline
            
            push cx
            
            push dx
            mov dx, cx
            sub dx, cols
            mov bx, dx
            add bx, bx                  ; 2 * bx
            mov ax, origArray [bx]
            pop dx
            
            push ax 
            
            push dx 
            mov dx, cx
            add dx, 1
            mov bx, dx
            add bx, bx                  ; 2 * bx
            mov ax, origArray [bx] 
            pop dx
            
            pop bx
            
            add ax, bx
            push ax 
            
            push dx
            mov dx, cx
            add dx, cols
            mov bx, dx
            add bx, bx
            mov ax, origArray [bx] 
            pop dx
            
            pop bx
            
            add ax, bx
            
            push ax
            
            push dx
            mov dx, cx
            sub dx, 1
            mov bx, dx
            add bx, bx
            mov ax, origArray [bx]
            pop dx
            
            pop bx
            
            add ax, bx 
            
            push ax
            
            ;call outputDecimal              ; call to print the sum of 4 neighbors
            ;call newline 
            
            pop ax
            ;mov ax, 16
            mov bl, divisor 
            div bl
            
            mov dh, ah        
            cbw
          
            push ax
                    
            ;call outputDecimal              ; call to average of 4 neighbors
            
            pop ax 
            
            mov bx, cx
            call replace
            
            pop cx
            
            add count, 1
            add cx, 1
            mov dx, count
            push ax
            mov ax, colsLimit
            add ax, 1
            cmp dx, ax
            pop ax        
            jge ifDone
            jl relax
            
            ifDone:
                add cx, 2
                push ax
                mov ax, sizeLimit
                cmp ax, cx 
                pop ax
                mov count, 1
                jge relax
                
                add iters, 1
                push ax
                mov ax, iters
                cmp ax, itersLimit
                pop ax
                je done
                ;call newline
                call printIterMsg
                call printRelArray
                call copyToOrigArray
                call printOrigArray
                mov count, 1
                mov cx, cols
                add cx, 1
                jmp relax    
                  
        done:
            ;call newline
            ;call printIterMsg  
            call printRelArray           
        
            pop dx
            pop cx
            pop bx
            pop ax
            
            ret
    endp jacobi
       
    proc replace
        add bx, bx
        mov newArray [bx], ax
        ret
    endp replace
    
    proc printIterMsg
        call newline     
        mov dx, offset msgIter       ; load and display the msgIter
        mov ah, 9
        int 21h
        
        call printIterNum
        
        ret
    endp printIterMsg
    
    proc printIterNum 
        push ax
        
        mov ax, iters
        call outputDecimal
        
        call newline
               
        pop ax       
               
        ret
    endp printIterNum
    
    proc copyToOrigArray
        push ax
        push bx
        
        mov bx, 0    
        
        copyOrig:
            push bx
            add bx, bx
            mov ax, newArray [bx]
            mov origArray [bx], ax
            
            pop bx 
            add bx, 1
            cmp bx, sizeArray
            jnz copyOrig
        
        pop bx
        pop ax
        
        ret
    endp copyToOrigArray
    
    proc copyToNewArray
        push ax
        push bx
        
        mov bx, 0    
        
        copyNew:
            push bx
            add bx, bx
            mov ax, origArray [bx]
            mov newArray [bx], ax
            
            pop bx 
            add bx, 1
            cmp bx, sizeArray
            jnz copyNew
        
        pop bx
        pop ax
        
        ret
    endp copyToNewArray
    
    proc printOrigArray
        push ax
        push bx
        push cx
        push dx 
        
        call newline     
        mov dx, offset msgOrigMat       ; load and display the msgNewMat
        mov ah, 9
        int 21h
        
        mov si, offset origArray        ; set SI = offset address of origArray
        push ax
        mov ax, rows
        mov bh, al                      ; set bh = number of rows
        mov ax, 0
        mov ax, cols
        mov bl, al                      ; set bl = number of columns
        pop ax
 
        call newline
        call print2DArray
        
        pop ax
        pop bx
        pop cx
        pop dx
        
        ret
    endp printOrigArray
    
    proc printNewArray
        push ax
        push bx
        push cx
        push dx 
        
        call newline     
        mov dx, offset msgNewMat        ; load and display the msgNewMat
        mov ah, 9
        int 21h
        
        mov si, offset newArray         ; set SI = offset address of newArray
        push ax
        mov ax, rows
        mov bh, al                      ; set bh = number of rows
        mov ax, 0
        mov ax, cols
        mov bl, al                      ; set bl = number of columns
        pop ax
 
        call newline
        call print2DArray
        
        pop ax
        pop bx
        pop cx
        pop dx
        
        ret
    endp printNewArray
    
    proc printRelArray
        push ax
        push bx
        push cx
        push dx 
        
        call newline     
        mov dx, offset msgRelMat       ; load and display the msgNewMat
        mov ah, 9
        int 21h
        
        mov si, offset newArray         ; set SI = offset address of newArray
        push ax
        mov ax, rows
        mov bh, al                      ; set bh = number of rows
        mov ax, 0
        mov ax, cols
        mov bl, al                      ; set bl = number of columns
        pop ax
 
        call newline
        call print2DArray
        
        pop ax
        pop bx
        pop cx
        pop dx
        
        ret
    endp printRelArray

    proc getInput
        mov bx, offset input
        mov count, 0
           
        getChar:
            mov ah, 01    
            int 21h                     ; CAPTURE ONE CHAR FROM KEYBOARD.
            mov [bx], al                ; STORE CHAR IN STRING.
            inc bx 
            inc count
            cmp al, 13
            jne getChar                 ; IF CHAR IS NOT "ENTER", REPEAT.           
        
            dec count                   ; NECESSARY BECAUSE CHR(13) WAS COUNTED.
            
            ; CONVERT STRING TO NUMBER. 
                mov bx , offset input   ; BX POINTS TO THE FIRST CHAR.
                add bx,  count          ; NOW BX POINTS ONE CHAR AFTER THE LAST ONE.
                mov bp, 0               ; BP WILL BE THE NUMBER CONVERTED FROM STRING.
                mov cx, 0               ; PROCESS STARTS WITH 10^0.
            
        getPowOf10:      
            cmp cx, 0
            je  firstPowOf10            ; FIRST TIME IS 1, BECAUSE 10^0 = 1.
            mov ax, 10
            mul cx                      ; CX * 10. NEXT TIME=100, NEXT TIME=1000...
            mov cx, ax                  ; CX == 10^CX.
            jmp charToNum               ; SKIP THE "firstPowOf10" BLOCK.
        
        firstPowOf10:    
            mov cx, 1                   ; FIRST TIME 10^0 = 1
            
        charToNum:       
            dec bx                      ; BX POINTS TO CURRENT CHAR.
            mov al, [bx]                ; AL = CURRENT CHAR.
            sub al, 48                  ; CONVERT CHAR TO NUMBER.
            
        ; MULTIPLY CURRENT NUMBER BY CURRENT POWER OF 10.
            mov ah, 0                   ; CLEAR AH TO USE AX.
            mul cx                      ; AX * CX = DX:AX. LET'S IGNORE DX.
            add bp, ax                  ; STORE RESULT IN BP. 
               
        ; CHECK IF THERE ARE MORE CHARS.    
            dec count
            cmp count, 0
            jne getPowOf10
            
        ret 
    endp getInput    

    proc print2DArray 
    ; this procedure will print the given 2D array
    ; input : SI=offset address of the 2D array
    ;       : BH=number of rows
    ;       : BL=number of columns 
    ; output : none
        
        push ax                         ; push bx onto the stack
        push cx                         ; push cx onto the stack
        push dx                         ; push dx onto the stack
        push si                         ; push si onto the stack
                                        
        mov cx, bx                      ; set cx=bx
    
        outer:                          ; loop label
            mov cl, bl                  ; set cl=bl
    
        inner:                          ; loop label
            mov ah, 2                   ; set output function
            mov dl, 20h                 ; set dl=20h
            int 21h                     ; print a character
                                 
            mov ax, [si]                ; set ax=[si]
                                
            call outputDecimal          ; call the procedure outputDecimal
    
            add si, 2                   ; set si=si+2
            dec cl                      ; set cl=cl-1
            jnz inner                   ; jump to label @inner_loop if cl!=0
                               
            mov ah, 2                   ; set output function
            mov dl, 0dh                 ; set dl=0dh
            int 21h                     ; print a character
    
            mov dl, 0ah                 ; set dl=0ah
            int 21h                     ; print a character
    
            dec ch                      ; set ch=ch-1
            jnz outer                   ; jump to label @outer_loop if cx!=0
                                    
       pop si                           ; pop a value from stack into si
       pop dx                           ; pop a value from stack into dx
       pop cx                           ; pop a value from stack into cx
       pop ax                           ; pop a value from stack into ax
    
       ret
    endp print2DArray

    proc outputDecimal
    ; this procedure will display a decimal number
    ; input : ax
    ; output : none

        push bx                             ; push bx onto the stack
        push cx                             ; push cx onto the stack
        push dx                             ; push dx onto the stack
    
        xor cx, cx                          ; clear cx
        mov bx, 10                          ; set bx=10
            
        toOutput:                           ; loop label
            xor dx, dx                         ; clear dx
            div bx                             ; divide ax by bx
            push dx                            ; push dx onto the stack
            inc cx                             ; increment cx
            or ax, ax                          ; take or of ax with ax
            jne toOutput                        ; jump to label @output if zf=0
    
            mov ah, 2                           ; set output function
    
        display:                            ; loop label
            pop dx                             ; pop a value from stack to dx
            or dl, 30h                         ; convert decimal to ascii code
            int 21h                            ; print a character
            loop display                        ; jump to label @display if cx!=0
    
        pop dx                              ; pop a value from stack into dx
        pop cx                              ; pop a value from stack into cx
        pop bx                              ; pop a value from stack into bx
    
        ret                                 ; return control to the calling procedure
    endp outputDecimal
 
    proc newline
        push ax
        push dx
        
        mov dl, 10
        mov ah, 02h
        int 21h
        mov dl, 13
        mov ah, 02h
        int 21h
                
        pop dx
        pop ax
                
        ret    
    endp newline
