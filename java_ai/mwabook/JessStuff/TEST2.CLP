;; File: ..\MWA\Jess\test2.clp
;; second example in CLIPS/Jess tutorial:

(deftemplate employee
    (slot first_name)
    (slot last_name)
    (slot age)
    (slot job_description
        (default programmer))
    (slot phone_number)
    (slot salary
        (default 60000))
    (slot phone_area_code
        (default 619)))

(defrule test_salary "test for a raise"
  ?fact1 <- (employee
               (last_name ?last)
               (salary ?money&:(< ?money 40000))
               (job_description programmer))
  =>
  (printout t ?last " does not make enough: " ?money crlf)
  (modify ?fact1 (salary (+ ?money 10000))))

(defrule startup "define initial WMEs created by (reset)"
  =>
  (assert (employee
             (first_name Carol)
             (last_name Watson)
             (job_description marketing)))
  (assert (employee
             (first_name Bowsworthy)
             (last_name Howonthroth)
             (salary 22000))))

(reset)
(run)
