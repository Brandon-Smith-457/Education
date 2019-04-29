let rec parseExp toklist sc =
  parseSExp
    toklist
    (fun toklist' exp -> match toklist' with
       | SEMICOLON :: toklist'' -> sc toklist'' exp
       | _ -> raise (Error "Expected a single semicolon"))

and parseSExp toklist sc = match toklist with
  | [] -> raise (Error "Empty")
  | toklist -> parsePExp toklist
                 (fun t exp -> match t with
                    | PLUS::t' -> parseSExp t'
                                    (fun t'' exp' -> sc t'' (Sum(exp, exp')))
                    | SUB::t' -> parseSExp t'
                                   (fun t'' exp' -> sc t'' (Minus(exp, exp')))
                    | t' -> sc t' exp)
    
and parsePExp toklist sc = match toklist with
  | [] -> raise (Error "Empty")
  | toklist -> parseAtom toklist
                 (fun t exp -> match t with
                    | TIMES::t' -> parsePExp t'
                                     (fun t'' exp' -> sc t'' (Prod(exp, exp')))
                    | DIV::t' -> parsePExp t'
                                   (fun t'' exp' -> sc t'' (Div(exp, exp')))
                    | t' -> sc t' exp)

and parseAtom toklist sc = match toklist with
  | [] -> raise (Error "Empty")
  | h::t ->
      match h with
      | INT(i) -> sc t (Int(i))
      | LPAREN -> parseSExp t 
                    (fun t' exp -> match t' with
                       | RPAREN::t'' -> sc t'' exp
                       | t'' -> raise (Error "No closing bracket"))
      | _ -> raise (Error "This is an invalid atom") 
      (*match toklist with
             | [] -> raise (Error "Empty")
             | h::t -> 
                 match h with
                 | INT (i) -> (fun toklist' exp -> +)
                 | LPAREN -> 
                 | _ -> raise (Error "This is an invalid atom")
*)

(* parse : string -> exp *)
let parse string =
  parseExp
    (lex string)
    (fun s e -> match s with
       | [] -> e
       | _ -> raise (Error "Incomplete expression"))

(* eval : string -> int *)
let eval e = eval' (parse e)
