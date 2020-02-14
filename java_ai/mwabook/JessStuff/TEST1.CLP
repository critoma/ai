;; File: ..\MWA\Jess\test1.clp
;; first example in CLIPS/Jess tutorial:

(deftemplate employee
    (slot first_name)
    (slot last_name)
    (slot age)
    (slot job_description
        (default programmer))
    (slot phone_number)
    (slot phone_area_code
        (default 619)))

(defrule print_programmer_names "test rule for tutorial 1"
  (employee
    (last_name ?last)
    (first_name ?first)
    (job_description programmer))
  =>
  (printout t "Programmer:" ?last ", " ?first crlf))

(defrule startup "define initial WMEs created by (reset)"
  =>
  (assert (employee
             (first_name Carol)
             (last_name Watson)
             (job_description marketing)))
  (assert (employee
             (first_name Mark)
             (last_name Watson)
             (job_description programmer))))

(reset)
(run)
