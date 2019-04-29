(* -------------------------------------------------------------*)
(* QUESTION 1 : String manipulation  [20 points]                *)
(* -------------------------------------------------------------*)

(* string_explode : string -> char list *)
let string_explode s =
  tabulate (String.get s) (String.length s)
  
(* string_implode : char list -> string *)
let string_implode l =
  let string_concat base a =
    base ^ (Char.escaped a)
  in
  List.fold_left string_concat "" l
    
(* -------------------------------------------------------------*)
(* QUESTION 2 : Insert a string into a dictionary  [20 points]  *)
(* -------------------------------------------------------------*)

(* Insert a word into a dictionary. Duplicate inserts are allowed *)

let  insert s t =
  (* ins: char list * char trie list -> char trie list *)
  let rec ins (x::l) t = match t with
    | [Empty] -> unroll (x::l)
    | [] -> unroll (x::l)
    | Empty::_::_ -> unroll (x::l)
    | Node(x', h)::t' ->
        if x = x' then [Node(x', (ins l h))]
        else Node(x', h)::(ins (x::l) t')
  in
  ins (string_explode s) t
(* -------------------------------------------------------------*)
(* QUESTION 3 : Look up a string in a dictionary   [20 points]  *)
(* -------------------------------------------------------------*)

(* Look up a word in a dictionary *)

let lookup s t =
  (* lkp : char list -> char trie list -> bool *)
  let rec lkp l t =
    raise NotImplemented
  in
  raise NotImplemented

(* -------------------------------------------------------------*)
(* QUESTION 4 : Find all strings in a dictionary   [OPTIONAL]   *)
(* -------------------------------------------------------------*)

(* Find all strings which share the prefix p *)

let find_all prefix t =
  (* find_all' : char list -> char trie list -> char list list *)
  let rec find_all' l t =
    raise NotImplemented
  in
  raise NotImplemented

(* -------------------------------------------------------------*)
(* QUESTION 5 :  Logic functions   [OPTIONAL]                   *)
(* -------------------------------------------------------------*)

(* eval: labeled_pred -> labeled_pred -> int -> int -> bool *)
let eval (_, (p : int -> bool)) (_, (q : int -> bool)) (n : int) =
  raise NotImplemented
