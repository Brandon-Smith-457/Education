(* ------------------------------------------------------------------------*)
(* Q 1 : Money in the bank (25 points)                                     *)
(* ------------------------------------------------------------------------*) 
let new_account p =
  let pass = ref (p : passwd) in
  let balance = ref 0 in
  let counter = ref 0 in
  {update_passwd = (fun p' p'' ->
       if p' = !pass then (counter := 0; pass := p'')
       else (counter := !counter + 1 ; raise wrong_pass));
   retrieve = (fun p' (a : int) -> 
       if !counter < 3 then
         (if p' = !pass then 
            (if a <= !balance  then (counter := 0 ; balance := !balance - a)
             else raise no_money)
          else (counter := !counter + 1 ; raise wrong_pass))
       else raise too_many_attempts);
   deposit = (fun p' (a : int) ->
       if !counter < 3 then
         (if p' = !pass then (counter := 0 ; balance := !balance + a)
          else (counter := !counter + 1 ; raise wrong_pass))
       else raise too_many_attempts);
   print_balance = (fun p' ->
       if !counter < 3 then
         (if p' = !pass then (counter := 0 ; !balance)
          else (counter := !counter + 1 ; raise wrong_pass))
       else raise too_many_attempts)}
;;


(* ------------------------------------------------------------------------*)
(* Q 2 : Memoization (75 points)                                           *)
(* ------------------------------------------------------------------------*)

(* Q 2.1 : Counting how many function calls are made *)

let rec catalan_I n = 
  let recur = ref 0 in 
  let rec catalan' n =
    (recur := !recur + 1 ;
     match n with
     | 0 -> 1
     | 1 -> 1
     | n -> sum (fun i -> catalan' i * catalan' (n - 1 - i)) (n - 1))
  in
  let res = catalan' n in
  {num_rec = !recur;
   result = res} 
;;


(* Q 2.2 : Memoization with a global store *)

let rec catalan_memo n = match Hashtbl.find_opt store n with
  | Some d -> d
  | None -> 
      let rec catalan n = match n with
        | 0 -> 1
        | 1 -> 1
        | n -> sum (fun i -> catalan_memo i * catalan_memo (n - 1 - i)) (n - 1)
      in
      (Hashtbl.add store n (catalan n) ; catalan_memo n)
;;


(* Q 2.3 : General memoization function *)

let memo f stats =
  let hash = Hashtbl.create 1000 in
  let rec f' a =
    match Hashtbl.find_opt hash a with
    | Some v -> incr stats.lkp; v
    | None ->
        let b = f f' a in
        incr stats.entries;
        Hashtbl.add hash a b;
        b
  in
  f'
;;

(* Q 2.4 : Using memo to efficiently compute the Hofstadter Sequence Q *)

let hof_Q q = function
  | 1 | 2 -> 1
  | n -> q (n - q (n - 1)) + q (n - q (n - 2))

let hofstadter_Q =
  let s = { lkp = ref 0; entries = ref 0 } in
  let memo_Q = memo hof_Q s in
  fun n -> (memo_Q n, s)
;;