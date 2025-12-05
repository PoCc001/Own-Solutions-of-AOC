let base_dir = "./"   (* insert base directory for inputs *)

let get_input = fun name -> In_channel.input_lines (In_channel.open_text (base_dir ^ name ^ ".txt"))

let rec get_input_line input i = match input with
    | [] -> None
    | h::t -> if i = 0 then Some h else get_input_line t (i-1)

let get_input_char_list input = List.flatten (List.map (fun s -> List.rev (String.fold_left (fun acc c -> c::acc) [] s)) input)
