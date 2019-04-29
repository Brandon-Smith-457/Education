(* ————————————————————–—————————————————————————————————————————————– *)
(* QUESTION 1 *)
(* Helper function: given two expressions, we add or multiply
   them     *)
(* ————————————————————–—————————————————————————————————————————————– *)

let add e1 e2 = 
  let n = ref ((get_value e1) + (get_value e2)) in
  Plus (n, e1, e2)
  (*
    match e1, e2 with 
   | Num (n1), Num (n2) -> let n = ref (!n1 + !n2) in 
       Plus (n, e1, e2)
   | Num (n1), Plus (n2, e2', e2'') -> let n = ref (!n1 + !n2) in 
       Plus (n, e1, e2)
   | Num (n1), Times (n2, e2', e2'') -> let n = ref (!n1 + !n2) in 
       Plus (n, e1, e2) 
   | Plus (n1, e1', e1''), Num (n2) -> let n = ref (!n1 + !n2) in 
       Plus (n, e1, e2)
   | Plus (n1, e1', e1''), Plus (n2, e2', e2'') -> let n = ref (!n1 + !n2) in 
       Plus (n, e1, e2)
   | Plus (n1, e1', e1''), Times (n2, e2', e2'') -> let n = ref (!n1 + !n2) in 
       Plus (n, e1, e2)
   | Times (n1, e1', e1''), Num (n2) -> let n = ref (!n1 + !n2) in 
       Plus (n, e1, e2)
   | Times (n1, e1', e1''), Plus (n2, e2', e2'') -> let n = ref (!n1 + !n2) in 
       Plus (n, e1, e2)
   | Times (n1, e1', e1''), Times (n2, e2', e2'') -> let n = ref (!n1 + !n2) in 
       Plus (n, e1, e2)
  *)
        
let mult e1 e2 = 
  let n = ref ((get_value e1) * (get_value e2)) in
  Times (n, e1, e2)

(* ————————————————————–—————————————————————————————————————————————– *)
(* QUESTION 2                                                        *)
(* compute_column m f = c

   Given a spreadsheet m and a function f, compute the i-th value in
   the result column c by using the i-th value from each column in m.

   Example:
   m = [ [a1 ; a2 ; a3 ; a4] ;
         [b1 ; b2 ; b3 ; b4] ;
         [c1 ; c2 ; c3 ; c4] ]

  To compute the 2nd value in the new column, we call f with
  [a2 ; b2 ; c2]

   Generic type of compute_column:
     'a list list -> ('a list -> 'b) -> 'b list

   If it helps, you can think of the specific case where we have a
   spreadsheet containing expressions, i.e.
   compute_column: exp list list -> (exp list -> exp) -> exp list

   Use List.map to your advantage!

   Carefully design the condition when you stop.
*)
(* ————————————————————–—————————————————————————————————————————————– *)
let rec compute_column sheet (_, f) = 
  List.map f (transpose sheet)
and transpose m = match m with
  | [] -> []
  | []::t -> transpose t
  | (h_h::h_t)::t ->
      (h_h :: List.map List.hd t) :: transpose (h_t :: List.map List.tl t)
    
(* ————————————————————–—————————————————————————————————————————————– *)
(* QUESTION 3 *)
(* Implement a function update which given an expression will re-
   compute the values stored at each node. This function will be used
   after we have updated a given number.

   update  : exp -> unit

*)
(* ————————————————————–—————————————————————————————————————————————– *)

let rec update expr = match expr with
  | Num (n) -> ()
  | Plus (n, e1, e2) -> (match add (update e1 ; e1) (update e2 ; e2) with
      | Plus (n', e1', e2') -> n := !n')
  | Times (n, e1, e2) -> (match mult (update e1 ; e1) (update e2 ; e2) with
      | Times (n', e1', e2') -> n:= !n')

let update_sheet sheet = List.map update
    (List.fold_left (fun acc column -> List.rev_append column acc) [] sheet) ; ()

(* EXTRA FUN:
   Our implementation traverses the whole expression and even worse
   the whole spreadsheet, if one number cell is being updated.

   If you are looking for a nice programming problem, think
   about how to update only those values which are parent nodes to the
   Number being updated. You might need to choose a different
   representation for expressions.

*)
(* ————————————————————–—————————————————————————————————————————————– *)
