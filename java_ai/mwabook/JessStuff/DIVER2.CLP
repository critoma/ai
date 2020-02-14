;; File: ..\MWA\Jess\diver2.clp
;; third example in CLIPS/Jess tutorial:

(deftemplate goal
 (slot type)
 (slot value (default N)))   ;; N for no, Y for yes
(deftemplate number_of_puncture_wounds
 (slot value (default 0)))
(deftemplate lacerations
 (slot value (default N)))

(defrule artificial_respiration
 ?fact1 <- (goal (type nobreath) (value "Y"))
 =>
 (printout t "Lay diver on his or her back in prepration "
  "to giving artificial repiration." crlf)
 (modify ?fact1 (value being_treated))
 (assert (goal (type respirate) (value "Y")))
 (printout t "Can you see any foreign objects in the" crlf
  "diver's mouth? (Y or N):")
 (assert (goal (type unplug_mouth) (value (readline)))))

(defrule unplug_the_mouth
 ?fact1 <- (goal (type unplug_mouth) (value "Y"))
 =>
 (printout t "Remove foreign matter from mouth of diver" crlf)
 (modify ?fact1 (value unplugged)))

(defrule start-respiration
 ?fact <- (goal (type respirate) (value "Y"))
 =>
 (printout t "Pull chin forward." crlf)
 (printout t "Open mouth wide and pinch nostrils shut." crlf)
 (printout t "REPEAT: blow air into mouth and pause to allow "
  "air to escape." crlf))
;; (modify ?fact1 (type OK)))

(defrule handle_bleeding
 ?fact1 <- (goal (type bleed) (value "Y"))
 =>
 (printout t "Enter number of puncture wounds (0, 1, or 2):")
 (assert (number_of_puncture_wounds (value (readline))))
 (printout t "Are there any lacerations? (Y or N):")
 (assert (lacerations (value (readline)))))

(defrule handle_punctures
 ?fact1 <- (number_of_puncture_wounds (value "2"))
 =>
 (printout t "Are the two punctures lose together with "
  "surrounding redness and stinging? (Y or N):")
 (assert (goal (type octopus_bite) (value (readline)))))

(defrule handle_octopus_bite
 ?fact <- (goal (type octopus_bite) (value "Y"))
 =>
 (printout t "Diver has probably been bitten by an octopus." crlf)
 (printout t "Apply a cold compress to the wound." crlf)
 (printout t "Keep the diver lying down." crlf)
 (printout t "Get medical attention as soon as possible." crlf)
 (printout t "Is the bleeding profuse? (Y or N):")
 (assert (goal (type bleeding_profuse) (value (readline)))))

(defrule handle_profuse_bleeding
 ?fact1 <- (goal (type bleeding_profuse) (value "Y"))
 =>
 (printout t "Apply pressure to the wound to slow the "
  "flow of blood." crlf))

(defrule handle_one_puncture
 ?fact1 <- (number_of_puncture_wounds (value "1"))
 =>
 (printout t "Are there any muscle spasms around the" crlf
  "puncture? (Y or N)")
 (assert (goal (type venom_puncture) (value (readline))))
 (printout t "Is there a severe burning sensation around" crlf
  "the wound? (Y or N)")
 (assert (goal (type cone_shell_puncture) (value "Y")))
 (printout t "Clean the puncture and apply pressure to" crlf
  "prevent bleeding." crlf))

(defrule venom_puncture
 ?fact1 <- (goal (type venom_puncture) (value "Y"))
 =>
 (printout t "The wound was likely caused by either a:" crlf
  "    Weever fish"    crlf
  "    Scorpion fish"  crlf
  "    Rat fish"       crlf
  "    Rabbit fish"    crlf  crlf
  "Irrigate with clean water and make a small" crlf
  "incision across the wound and apply suction" crlf
  "before soaking in very hot water for 30" crlf
  "minutes.  Obtain medical care as soon as possible." crlf
  "This temporary treatment should alleviate the pain" crlf
  "and help to combat the effects of venom to prevent" crlf
  "a secondary infection." crlf))


(defrule cone_shell_puncture
 ?fact1 <- (goal (type cone_shell_puncture) (value "Y"))
 =>
 (printout t "The wound is probably a cone shell puncture." crlf
  "Keep the diver lying down and get medical care" crlf
  "immediately." crlf)
 (modify ?fact1 (value no_treatment)))

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
