(* Q1:
   pow n k: Raises the number n to the power of k,
            provided k is a non-negative integer.
            Raises exception Domain otherwise.

   Examples:
   pow 0  5 ==> 0
   pow 5  0 ==> 1
   pow 2  3 ==> 8
   pow 2 -3 ==> Exception: Domain

*)

let pow (n : int) (k : int) =
  let rec pow' (n : int) (k : int) = match k with
    | 0 -> 1
    | _ -> n * (pow' n (k - 1))
  in
  if k >= 0 then
    pow' n k
  else
    raise Domain


(* Q1:

  fib n : Computes the n-th element of the Fibonacci sequence:

   1, 1, 2, 3, 5, ..., fib (n-1) + fib (n -2)

   Raises exception Domain if n is negative.

  Examples:

  fib 0 => 1
  fib 10 => 89
  fib -1 => Exception: Domain

*)

let rec fib (n : int) =
  let rec fib' (n : int) = match n with
    | 0 -> 1
    | 1 -> 1
    | n -> fib' (n - 1) + fib' (n - 2)
  in
  if n >= 0 then fib' n
  else raise Domain


(* Q2: Newton-Raphson method for computing the square root
*)
let square_root a =
  let rec findroot x acc =
    let x' = (((a/.x) +. x) /. 2.0) in
    if (abs_float (x' -. x) < acc) then match x with 
      | _ -> x'
    else match x with
      | _ -> findroot (((a/.x) +. x) /. 2.0) acc
  in
  if a > 0.0 then
    findroot 1.0 epsilon_float
  else
    raise Domain

(* Q3: Tail-recursive version of power function *)
      (* This works but it's ugly
      let pow_tl n k =
         let rec aux n k acc =
           if k != 0 then
             let acc = n * acc in
             match k with
             | _ -> aux n (k - 1) acc 
           else match k with
             | _ -> acc
         in
         aux n k 1
*)

let pow_tl n k =
  let rec aux n k acc = match k with
    | 0 -> acc 
    | _ -> aux n (k - 1) (n * acc)
  in
  aux n k 1
    
    
(* Q4: Checking naively whether a given integer is a prime number *)

let is_prime n =
  let rec check_x n x =
    if ((x * x) >= n) then
      true
    else if (n mod x = 0) then
      false
    else
      check_x n (x + 1)
  in
  if (n > 1) then
    check_x n 2
  else
    raise Domain


(* Q5: Computing the greatest common divisor using Euclid's algorithm *)

let gcd a b = 
  let rec gcd_rec a b =
    if (b > a) then
      gcd_rec b a
    else
      let r = a mod b in
      if (r = 0) then
        b
      else
        gcd_rec b r
  in
  if (a = 0 && b != 0) then
    b
  else if (b = 0 && a != 0) then
    a
  else if (a >= 0 && b >= 0) then
    gcd_rec a b
  else
    raise Domain
    

