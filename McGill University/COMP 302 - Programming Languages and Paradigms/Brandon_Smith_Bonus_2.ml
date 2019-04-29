(* Question 1: let's compose something! *)

(* 1.1 Composition *)

let compose (fs : ('a -> 'a) list) : 'a -> 'a =
  fun x -> List.fold_right (fun y -> y) fs x

(* 1.2 Replication *)

let replicate (n : int) : 'a -> 'a list =
  let rec replicate' n x = match n with
    | 0 -> []
    | n -> x::(replicate' (n-1) x)
  in
  fun x -> replicate' n x
  (* 1.3 Repeating *)

let repeat (n : int) (f : 'a -> 'a) : 'a -> 'a =
  compose (replicate n f)

(* Question 2: unfolding is like folding in reverse *)

(* 2.1 Compute the even natural numbers up to an exclusive limit. *)
let evens (max : int) : int list = 
  unfold (fun b -> (b, b+2)) (fun b -> max <= b) 0

(* 2.2 Compute the fibonacci sequence up to an exclusive limit. *)
let fib (max : int) : int list =
  unfold (fun (i, j) -> (j, (j, i+j))) (fun (i, j) -> max <= j) (0, 1)
  
(* 2.3 Compute Pascal's triangle up to a maximum row length. *)
let pascal (max : int) : int list list =
  unfold (fun l -> (l, (List.map2 (fun x y -> x + y) (0::l) (List.append l [0])))) (fun l -> max < (List.length l)) [1]
(* 2.4 Implement the zip, which joins two lists into a list of tuples.
 * e.g. zip [1;2] ['a', 'c'] = [(1, 'a'); (2, 'c')]
 * Note that if one list is shorter than the other, then the resulting
 * list should have the length of the smaller list. *)
let zip (l1 : 'a list) (l2 : 'b list) : ('a * 'b) list = match l1, l2 with
  | [], _ -> []
  | _, [] -> []
  | x::xs, y::ys ->
      let max = (if (List.length l1) < (List.length l2) then List.length l1
                 else List.length l2) in
      unfold
        (fun n -> ((List.nth l1 n, List.nth l2 n), n+1))
        (fun n -> max <= n)
        0
      
    

