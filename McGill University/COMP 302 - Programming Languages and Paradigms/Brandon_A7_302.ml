let rec parseExp toklist = match toklist with
  | [] -> raise (Error "Expected an expression: Nothing to parse")
  | _ ->
      try parseSExp toklist
      with
      | SumExpr (exp, [SEMICOLON]) -> exp
      | _ -> raise (Error "Expected a single semicolon")

and parseSExp toklist = match toklist with
  | [] -> raise (Error "Expected an expression: Nothing to parse")
  | toklist -> (try parsePExp toklist with
      | ProdExpr (exp, PLUS::t) -> (try parseSExp t with
          | SumExpr (exp', t') -> raise (SumExpr (Sum (exp, exp'), t')))
      | ProdExpr (exp, SUB::t) -> (try parseSExp t with
          | SumExpr (exp', t') -> raise (SumExpr (Minus (exp, exp'), t')))
      | ProdExpr (exp, t) -> raise (SumExpr (exp, t))) 
            
and parsePExp toklist = match toklist with
  | [] -> raise (Error "Expected an expression: Nothing to parse")
  | toklist -> (try parseAtom toklist with
      | AtomicExpr (exp, TIMES::t) -> (try parsePExp t with
          | ProdExpr (exp', t') -> raise (ProdExpr (Prod (exp, exp'), t')))
      | AtomicExpr (exp, DIV::t) -> (try parsePExp t with
          | ProdExpr (exp', t') -> raise (ProdExpr (Div (exp, exp'), t')))
      | AtomicExpr (exp, t) -> raise (ProdExpr (exp, t))) 

and parseAtom toklist = match toklist with
  | [] -> raise (Error "Expected an expression: Nothing to parse")
  | h::t ->
      match h with
      | INT (i) -> raise (AtomicExpr (Int (i), t)) 
      | LPAREN -> 
          (try parseSExp t with
           | SumExpr (exp, RPAREN::t) -> raise (AtomicExpr (exp, t)))
      | _ -> raise (Error "This is an invalid atom")
;;

(* parse : string -> exp *)
let parse string =
  parseExp (lex string) ;;

(* eval : string -> int *)
let eval e = eval' (parse e) ;;
