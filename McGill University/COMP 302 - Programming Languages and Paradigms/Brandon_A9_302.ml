(* ---------------------------------------------------- *)

(* Processing finite objects lazily is also useful;
   it corresponds to demand driving compution.
*)
(* ---------------------------------------------------- *)
(* We define next a lazy list; this list is possibly
   finite; this is accomplished by a mutual recursive
   datatype.

   'a lazy_list defines a lazy list; we can observe the 
   head and its tail. For the tail we have two options:
   we have reached the end of the list indicated by the 
   constructor None or we have not reached the end 
   indicated by the constructor Some and we expose
   another lazy list of which we can observe the head and the tail.  

*)

(* ---------------------------------------------------- *)         
(* Q1 *)

(* 
   val take : int -> 'a lazy_list -> 'a list 
*)
let rec take n s = match n with
  | 0 -> []
  | n -> match force s.tl with
    | None -> s.hd::[]
    | Some tl -> s.hd::(take (n-1) tl)

(* val map : ('a -> 'b) -> 'a lazy_list -> 'b lazy_list
*)
let rec map f s = 
  {
    hd = f s.hd;
    tl = Susp (fun () -> match force s.tl with
        | None -> None
        | Some tl -> Some (map f tl))
  }

(* 
  val append : 'a lazy_list -> ('a lazy_list) option susp -> 'a lazy_list
*)
let rec append s1 (s2 : ('a lazy_list) option susp) =
  {
    hd = s1.hd;
    tl = (match force s1.tl with
        | None -> s2
        | Some tl -> Susp (fun () -> Some (tl)))
  } 

(* ---------------------------------------------------- *)
(* val interleave : 'a -> 'a list -> 'a list lazy_list *)
let rec insertAtIndex x l i = match l with
  | [] -> [x]
  | h::t -> if i = 0 then x::l
      else h::(insertAtIndex x t (i-1))
              
let rec interleave x l = 
  let rec interleave' x l i = 
    {
      hd = insertAtIndex x l i;
      tl = Susp (fun () -> if (List.length l) > i
                  then Some (interleave' x l (i + 1))
                  else None)
    }
  in
  interleave' x l 0
  
(* ---------------------------------------------------- *)
(* val flatten : 'a lazy_list lazy_list -> 'a lazy_list = <fun>
*)
let rec flatten s = 
  {
    hd = s.hd.hd;
    tl = match force s.tl with
      | None -> Susp (fun () -> None)
      | Some tl -> Susp (fun () -> Some (append s.hd (Susp (fun () -> Some (tl.hd))))) 
  }
      (*
{
    hd = s.hd.hd;
    tl = Susp (fun () -> match force s.tl with
        | None -> (match force s.hd.tl with
            | None -> None
            | Some tl' -> Some (tl'))
        | Some tl -> (match force s.hd.tl with
            | None -> Some (flatten tl)
            | Some tl' -> Some (tl')))
  }
*)

(* ---------------------------------------------------- *)
(* Permute *)
let rec permute l = raise NotImplemented

(* ---------------------------------------------------- *)         
(* Q2 *)
                   
let rec hailstones n = raise NotImplemented
    