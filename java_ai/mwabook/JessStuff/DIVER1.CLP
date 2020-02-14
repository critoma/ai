;; File: ..\MWA\Jess\diver1.clp
;; third example in CLIPS/Jess tutorial:

(deftemplate goal
 (slot type)
 (slot value (default N)))   ;; N for no, Y for yes

(defrule artificial_respiration
 ?fact1 <- (goal (type nobreath) (value "Y"))
 =>
 (printout t "Lay diver on his or her back in prepration "
  "to giving artificial repiration." crlf)
 (modify ?fact1 (value being_treated))
 (assert (goal (type respirate) (value "Y"))))


(defrule start-respiration
 ?fact <- (goal (type respirate) (value "Y"))
 =>
 (printout t "Pull chin forward." crlf)
 (printout t "Open mouth wide and pinch nostrils shut." crlf)
 (printout t "REPEAT: blow air into mouth and pause to allow "
  "air to escape." crlf))

(defrule handle_bleeding
 ?fact1 <- (goal (type bleed) (value "Y"))
 =>
 (printout t "Apply pressure to stop bleeding." crlf))

(defrule handle_scrape
 (goal (type scrape) (value "Y"))
 =>
 (printout t "Gently wash the scraped area with soap and " crlf
  "clean water." crlf))

(defrule startup "define initial WMEs created by (reset)"
 =>
 (printout t "Is the diver bleeding? (Y or N):")
 (assert (goal
          (type bleed)
          (value (readline))))
 (printout t "Is the diver scraped? (Y or N):")
 (assert (goal (type scrape) (value (readline))))
 (printout t "Has the diver stopped breathing? (Y or N):")
 (assert (goal (type nobreath) (value (readline)))))

(reset)
(run)
