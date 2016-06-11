; multi-segment executable file template.
; assumes users follow instructions

data segment
    ; pre-loaded strings
    pkey    db "press any key...$"
    gSize   db "Enter size(powers of 2): $"
    gData   db "Enter data(5 characters): $"
    gEntry  db "Enter where data is coming from: $"
    gIns    db "Enter instructions: $"
    wStage  db "Stage: $"
    wCurRow db "Current row: $"
    wData   db "Data: $" 
    wNxtIns db "Next Instruction: $"
    
    ;user input
	Usize db 4h		;Added from Filmification v1.0
    Udata    db 42h,42h,42h,42h,42h, "$"
    Uentry   db 04h
    Uins     db 01h,00h,"$"
                       
    ;helper variables
    tempString  db ?                                    ;for printf macro
    scanfNum    db ?                                    ;for scanfNum macro
    scanfStr    db ?                                    ;for scanfStr macro
    log_out     db ?                                    ;data holder for log2_calc output
    stages      db ?                                    ;num of stages
    total       db ?                                    ;total cells 
    dat_seg     dw 0600h                                ;data segment
    dat_ori     dw 0700h                                ;main segment
    dat_ind     dw 0000h                                ;data index
    id          db 00h                                  ;ID of cell
    id_mem      dw ?                                    ;holder of current id memory location
    num_holder  db ?                                    ;for num_expt/determine_direction/read_ins 
    num_holder2 db ?                                    ;for determine_next_step, printInstruction macro
    num_holder3 dw ?                                    ;for writing data/storing ax in writing UI
    cur_stage   db 1                                    ;used to know which stage the writing is already. refer to writing part
    count       db ?                                    ;used to tracking writing and in printInstruction macro
    cur_row     db ?                                    ;used by determine_direction and determine_next_step and UI
    last_stage  db ?                                    ;used to determine if cell is in last stage or not 
    dat_size    db 0Ah                                  ;size of every data (id + data + pointer + pointer)
    cur_id      dw 0000h                                ;current id of cell being viewed/visited/something
    data_len    db 05h                                  ;length of data(static)
    

ends

stack segment
    dw   128  dup(0)
ends

helper_funcs segment
printf MACRO tempString                                 ;print
    lea     dx,tempString
    mov     ah,09h
    int     21h
endm
scanfNumber MACRO                                       ;scanf but for numbers only. stores input in scanfNum
    local scanf_loop, writing_done
    
    mov     scanfNum,00h
    mov     cl,00h
    mov     bl,00h
    scanf_loop:
    
    mov     ah,01h
    int     21h
    
    cmp     al,0Dh
    je      writing_done
    
    mov     ah,00h
    sub     al,30h
    mov     cl,al
    mov     al,0Ah
    mul     bl
    mov     bl,al
    add     bl,cl
    jmp     scanf_loop 
    
    writing_done:
    mov     scanfNum,bl
    mov     bl,00h 
endm         
scanfString MACRO                                       ;scanf but for strings only. stores input in scanfStr
    local scanf_loop, writing_done
    
    mov     cl,00h
    lea     di,scanfStr
    scanf_loop:
    
    mov     ah,01h
    int     21h
    
    cmp     al,0Dh
    je      writing_done
    
    stosb     
    jmp     scanf_loop 
    
    writing_done:
    mov     al,24h
    stosb 
endm
copyString MACRO str1, str2                             ;copies string from str1 to str2
    local copyLoop, copyDone
    mov     si,OFFSET str1
    mov     di,OFFSET str2
    
    copyLoop:
    cmp     [si],24h
    je      copyDone
        
    mov     dh,[si]
    mov     dl,[di]    
    mov     al,[si]
    mov     [di],al
    inc     di
    inc     si     
                
    jmp     copyLoop
        
    copyDone:
    mov     al,[si]
    mov     [di],al
endm 
nextLine MACRO                                          ;puts cursor to next line
    mov     dx,13                                      
    mov     ah,2
    int     21h
      
    mov     dx,10
    mov     ah,2
    int     21h
endm
num_expt MACRO 
    ;powers of 2 only
    local expt_loop, expt_loop_done
    mov     cx,00h
    mov     bl,02h
    mov     al,02h                                                                                                                        
    mov     cl,cur_stage
    sub     cl,01h
    expt_loop:
    jcxz    expt_loop_done
    mul     bl
    sub     cl,01h
    jmp     expt_loop
    expt_loop_done:
    mov     num_holder,al
    mov     al,00h 
    mov     bl,00h
endm      
num_ceil MACRO
    ;use with div only
    ;if ah(modulo) != 0, al + 1
    ;else, al
    local noAdjust
    cmp     ah,00h
    je      noAdjust
    mov     ah,00h
    add     al,01h
    noAdjust:
endm
getStage MACRO
    ;get stage from given ID
    ;output: stage -> al
    local getstageloop
    mov     al,00h
    mov     cl,00h
   
    getstageloop:
    cmp     Usize,al
    jl      exitloop
    add     al,Usize
    add     cl,01h
    jmp     getstageloop
    
    exitloop:
    mov     al,cl
    mov     cl,00h
endm
log2_calc MACRO                                         ;calculates log(base 2) num. num must be perfect log(no decimals).                                             
    mov     bl,02h                                      ;output stored in cl
    mov     al,02h                                             
    mov     cl,01h
     
    log_loop:  
    cmp     al,dh
    je      log_exit
    mul     bl
    inc     cl
    jmp     log_loop
  
    log_exit:
    mov     log_out,cl
    mov     ax,00h
    mov     bx,00h
    mov     cx,00h
    mov     dx,00h
endm
determine_direction MACRO                               ;returns 0 for down, 1 for up
    num_expt
    mov     al,Usize
    mov     bl,num_holder
    div     bl
    mov     num_holder,al
    mov     al,cur_row
    mov     bl,num_holder
    div     bl
    num_ceil
    mov     num_holder,al 
    mov     bl,02h
    div     bl 
    cmp     ah,00h
    mov     ax,00h
    mov     bx,00h
    je      even
    mov     num_holder,1
    jmp     determine_direction_done
    even:
    mov     num_holder,0
    determine_direction_done:
endm
determine_next_step MACRO                               ;calculates which Usize to go to next, places output to num_holder
    num_expt
    mov     al,Usize
    mov     bl,num_holder
    div     bl
    mov     num_holder2,al
    mov     ax,00h
    mov     bx,00h 
    determine_direction

    mov     al,num_holder
    cmp     al,00h
    mov     al,cur_row
    mov     bl,num_holder2
    je      goDown  
    
    goUp:
    add     al,bl
    mov     num_holder,al
    jmp     goUp_done   
    
    goDown:
    sub     al,bl
    mov     num_holder,al
    mov     al,cur_row
    jmp     goDown_done
    
    goUp_done:
    goDown_done:
    mov     ax,00h
    mov     bx,00h                                                   
endm 
setCursor MACRO x, y
    mov     bh,0
    mov     ah,02h
    mov     dh,y
    mov     dl,x
    int     10h
endm
clrscr MACRO
    setCursor 0, 0
    
    mov     ax,0600h
    mov     bh,0Fh
    mov     cx,0
    mov     dx,8025
    int     10h
endm
printHex MACRO hexValue                                 ;print in display a single hex value
    local letter1, number1, print1, letter2, number2, print2 
    mov     bx,0010h
    mov     al,hexValue
     
    mov     ah,00h
    cmp     al,10d 
    div     bl
            
    mov     bh,ah
    cmp     al,09h
    jle     number1
    
    letter1:
    sub     al,09h
    add     al,40h
    mov     dl,al                  
    jmp     print1
    
    number1:
    add     al,30h
    mov     dl,al
    jmp     print1
    
    print1:
    mov     ah,02h
    int     21h
    
    mov     al,bh
    cmp     al,09h
    jle     number2
    
    letter2:
    sub     al,09h
    add     al,40h
    mov     dl,al
    jmp     print2                  
    
    number2:
    add     al,30h
    mov     dl,al
    jmp     print2
              
    print2:          
    mov     ah,02h
    int     21h
endm
printInstruction MACRO cur_ins, string
    local loop1, color_grey, color_black, color_green, loopDone
    mov     count,00h
    mov     di,00h
    mov     cx,0001h
    mov     bh,cur_ins
    mov     num_holder2,bh
    mov     bx,0000h
                      
    loop1:
    mov     bl,count
    cmp     bl,num_holder2
    jl      color_grey
    je      color_green
    jg      color_black
    
    color_grey:
    mov     bl,08h
    jmp     print
    color_green:
    mov     bl,0Ah
    jmp     print
    color_black:
    mov     bl,0Fh 
    jmp     print
    
    print:
    mov     dx,0000h
    mov     dh,num_holder2                              ;you can use the stage number for reference on what row to print
    mov     dl,count
    add     dl,3Eh 
    mov     ah,02h
    int     10h
    
    mov     al,string[di]
    cmp     al,24h                                      ;'$'
    je      loopDone
    
    mov     ah,09h
    int     10h
                   
    inc     di
    inc     count               
    jmp     loop1
    loopDone:
endm
ends                                                       

code segment
start:
; set segment registers:
    mov     ax,data
    mov     ds,ax
    mov     es,ax
    
    mov     ax,03h
    int     10h

    ; code below ; 
       
    ;get data from user---------------------------------








    
    lea     dx,gData
    mov     ah,09h
    int     21h
    
    scanfString
    copyString scanfStr, Udata 
    nextLine
    
    lea     dx,gEntry
    mov     ah,09h
    int     21h
    
    scanfNumber
    mov     al,scanfNum
    mov     Uentry,al
    nextLine
    
    lea     dx,gIns
    mov     ah,09h
    int     21h
    
    scanfString
    copyString scanfStr, Uins 
    clrscr
    
    ;---------------------------------------------------
    
    ;prep data------------------------------------------
    mov     dh,Usize                                    ;determine num of stages, store into variable 'stages'
    log2_calc
    
    mov     al,log_out
    inc     al                                      
    mov     stages,al
    mul     Usize
    mov     total,al                                    ;determine total cells
    mov     ax,00h
    ;---------------------------------------------------
    
    ;write init data/values-----------------------------
    mov     bl,total                                    ;prep for writing
    mov     di,[dat_ind]                                ;0000
    mov     ax,dat_seg
    mov     es,dat_seg                                  ;0600
    
    mov     al,total
    sub     al,Usize
    mov     last_stage,al
    mov     cur_row,00h 
    
    jmp     writ_loop
    
    writ_loop:
    add     cur_row,01h
    
    mov     cl,cur_row
    cmp     cl,Usize
    jle     proceed_writing
    mov     cur_row,01h
    add     cur_stage,01h                       
    
    proceed_writing:                       
    mov     id_mem,di
    add     id,01h                                      ;write ID
    mov     al,id               
    mov     cl,01h
    rep     stosb 
    
    mov     al,20h                                      ;write blank (Udata here)
    mov     cl,05h
    rep     stosb
    
    mov     cl,id                                       ;skip part below if already at last stage
    cmp     cl,last_stage       
    jg      lastStage 
      
    mov     ax,000Ah                                    ;write pointer (straight path)
    mul     Usize
    add     id_mem,ax           
    mov     dx,id_mem
    mov     al,dh
    mov     cl,01h
    stosb
    mov     al,dl
    mov     cl,01h
    stosb                        
    
    mov     count,bl                                    ;write pointer (alternate path)
    determine_next_step

    mov     al,Usize
    mul     cur_stage
    add     al,num_holder
    sub     al,01h
    mul     dat_size
    mov     dx,ax
    mov     al,dh
    mov     cl,01h
    stosb
    mov     al,dl
    mov     cl,01h
    stosb
    mov     bl,count        
    
    jmp     continue_writing
    
    lastStage:                                          ;write null if last stage        
    mov     al,00h
    mov     cl,01h
    stosb
    
    mov     al,00h
    mov     cl,01h
    stosb         
    
    mov     ax,0000h            
    mov     cl,01h
    stosw
    
    continue_writing:
    sub     bl,01d
    cmp     bl,0
    jne     writ_loop
    ;---------------------------------------------------
    
    ;read ins and pass data-----------------------------  
    mov     bx,0001h                                    ;for traversing Uins
    mov     ax,000Ah                                    ;for storing curr_id
    mov     cx,0000h                                    ;just for the next lines below
    mov     dx,0000h                                    ;for temp storage, see pass_done
    mov     num_holder,00h
    mov     cur_stage,00h                               
    mov     es,dat_seg                                  ;0600
    mov     di,[dat_ind]                                ;0000
        
    mov     cl,Uentry
    sub     cl,01h
    mul     cx
    mov     cx,0000h
         
    read_loop:                                          ;read Uins loop + write Udata into cell
    mov     dx,0000h
    mov     cur_id,ax                                   ;find current row
    mov     bx,000Ah
    div     bx
    mov     bl,Usize
    div     bl
    mov     cur_row,ah
    inc     cur_row                                     ;0 == row 1
    mov     dx,0000h
    mov     ax,cur_id
    
    mov     di,cur_id
    mov     bl,num_holder
    cmp     bl,stages
    je      read_loop_done
    
    mov     cl,Uins[bx]
    inc     bl
    mov     num_holder,bl  
    
    cmp     cl,31h
    je      take_alternate
    jne     take_straight
    
    take_straight:
    add     ax,0006h       
    jmp     pass_done
    
    take_alternate:
    add     ax,0008h
    jmp     pass_done
            
    pass_done:
    mov     dx,ds
    mov     ds,dat_seg
    mov     di,ax
    mov     ah,[di]
    inc     di
    mov     al,[di]
    mov     ds,dx
    mov     num_holder3,ax
    
    mov     num_holder2,cl
    mov     ax,0000h                                    ;write Udata
    mov     bx,0000h
    add     cur_id,01h          
    lea     ax,Udata
    
    write_data: 
    mov     al,Udata[bx]
    mov     cl,01h
    mov     di,cur_id
    stosb
    
    add     cur_id,01h
    add     bl,01h
    cmp     bl,05h
    jne     write_data
    
    mov     al,cur_stage
    setCursor 0,al
    printf wStage
    mov     al,cur_stage
    setCursor 7,al
    printHex al
    mov     al,cur_stage
    setCursor 12,al
    printf wCurRow
    printHex cur_row
    mov     al,cur_stage
    setCursor 30,al
    printf wData
    printf Udata
    mov     al,cur_stage
    setCursor 44,al
    printf wNxtIns
    printInstruction cur_stage, Uins
    inc     cur_stage 
    nextLine
    
    
    ;write done
    
    mov     cl,num_holder2
    mov     ax,num_holder3 
    
    jmp     read_loop
    
    read_loop_done:
    ;---------------------------------------------------
    
    exit:
                                                                             
    ; code above ;
            
    lea     dx,pkey
    mov     ah,09h
    int     21h      ; output string at ds:dx
    
    ; wait for any key....    
    mov     ah,01h
    int     21h
    
    mov     ax,4c00h ; exit to operating system.
    int     21h    
ends

end start ; set entry point and stop the assembler.
