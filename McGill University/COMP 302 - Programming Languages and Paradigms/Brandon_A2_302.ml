(* --------------------------------------------------------------------*)
(* QUESTION 1: House of Cards                                          *)
(* --------------------------------------------------------------------*)

(* Q1: Comparing cards *)
(* Comparing two ranks *)
let dom_rank (r1 : rank) (r2 : rank) = match r1, r2 with
  | Ace, _ -> true
  | King, Queen | King, Jack | King, Ten | King, Nine | King, Eight | King, Seven | King, Six -> true
  | Queen, Jack | Queen, Ten | Queen, Nine | Queen, Eight | Queen, Seven | Queen, Six -> true
  | Jack, Ten | Jack, Nine | Jack, Eight | Jack, Seven | Jack, Six -> true 
  | Ten, Nine | Ten, Eight | Ten, Seven | Ten, Six -> true 
  | Nine, Eight | Nine, Seven | Nine, Six -> true 
  | Eight, Seven | Eight, Six -> true 
  | Seven, Six -> true 
  | r1, r2 -> r1 = r2 
              
(* Comparing two cards (r1, s1) and (r2, s2) *)
let dom_card (c1 : card) (c2 : card) = match c1, c2 with
  | (r1, s1), (r2, s2) ->
      if s1 = s2 then dom_rank r1 r2
      else dom_suit s1 s2
        
        (* Q2: Insertion Sort – Sorting cards in a hand *)
let rec insert (c : card) (h : hand) : hand = match c, h with
  | _, Empty -> Hand(c, Empty)
  | c, Hand(c', h') -> if dom_card c c' then Hand(c, h)
      else Hand(c', insert c h') 
  
let rec sort (h : hand) : hand = match h with
  | Empty -> Empty
  | Hand(c', h') -> insert c' (sort h')
  
  
(* Q3: Generating a deck of cards *)
let generate_deck (suits : suit list) (ranks : rank list) : card list = 
  if suits = [] || ranks = [] then []
  else
    let rec outer_for_loop (suits : suit list) (ranks : rank list) : card list = 
      let rec inner_for_loop (s1 :suit) (ranks : rank list) : card list =
        match ranks with
        | [] -> []
        | r1::ranks2 -> (r1, s1)::inner_for_loop s1 ranks2
      in
      match suits with
      | [] -> []
      | s1::suits2 -> (inner_for_loop s1 ranks) @ (outer_for_loop suits2 ranks)
    in
    outer_for_loop suits ranks
  
(* Q4: Shuffling a deck of cards *)
let split (deck : card list) (n : int) : card * card list =
  let rec get_card (deck : card list) (n : int) : card = 
    match deck, n with
    | [], _ -> (Ace, Spades)
    | c::deck, 0 -> c
    | c::deck, n -> get_card deck (n - 1)
  in
  let rec remove_card (deck : card list) (n : int) : card list =
    match deck, n with
    | [], _ -> []
    | c::deck, 0 -> remove_card deck (n - 1)
    | c::deck, n -> c::(remove_card deck (n - 1))
  in
  ((get_card deck n), (remove_card deck n))
  
let shuffle (deck : card list) : card list =
  if deck = [] then []
  else
    let size = List.length deck in
    let rec select deck n = match (split deck (Random.int n)), n with
      | (c, []), _ -> c::[]
      | (c, deck), n -> c::(select deck (n - 1))
    in
    select deck size

(* --------------------------------------------------------------------*)
(* QUESTION 2: Sparse Representation of Binary Numbers                 *)
(* ------------------------------------------------------------------- *)

(* Q1: Incrementing a sparse binary number *)
let inc (ws : nat) : nat =
  let rec aux (ws : nat) : nat = match ws with
    | [] -> []
    | a::b::ws ->
        if a = b then aux ((a + b)::ws)
        else a::b::ws
    | c::[] -> c::[]
  in
  aux (1::ws)

(* Q2: Decrementing a sparse binary number *)
let dec (ws : nat) : nat =
  let rec aux (ws : nat) : nat = match ws with
    | [] -> []
    | a::ws ->
        if a = 1 then ws
        else aux ((a/2)::(a/2)::ws)
  in
  if ws = [] then raise Domain
  else aux ws

(* Q3: Adding sparse binary numbers *)
let rec add (m : nat) (n : nat) : nat  =
  let rec aux (ws : nat) : nat = match ws with
    | [] -> []
    | a::b::ws ->
        if a = b then aux ((a + b)::ws)
        else a::b::ws
    | c::[] -> c::[]
  in
  match m, n with
  | [], n -> n
  | m, [] -> m
  | a::m, b::n -> 
      if a = b then
        add (aux ((a + b)::m)) n
      else if a < b then
        a::(add m (b::n))
      else
        b::(add (a::m) n)

(* Q4: Converting to integer - tail recursively *)
let rec toInt (n : nat) (acc : int) : int = match n with
  | [] -> acc
  | a::n -> toInt n (acc + a)

let sbinToInt (n : nat) : int =
  toInt n 0
