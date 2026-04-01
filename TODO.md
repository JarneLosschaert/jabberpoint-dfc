# TODO - JabberPoint Extra Feature (Inhoudsopgave)

Dit bestand zet alle taken in werkvolgorde, gebaseerd op de opdrachttekst.

## Fase 0 - Basis en afbakening - Check

1. Bevestig scope met team
- Extra feature: automatische inhoudsopgave-slides op plekken waar `toc` voorkomt.
- Elke slide krijgt een onderwerp (`subject`).
- Onderwerp komt 1x in de inhoudsopgave per aaneengesloten onderwerp-blok.
- Optioneel: huidig onderwerp visueel markeren in inhoudsopgave.

2. Beslis en documenteer ontwerpkeuzes
- Wat als slide geen `subject` heeft?
  - Keuze vastleggen: fallback naar titel of expliciet "Onbekend onderwerp".
  - Kueze: direct "Onbekend onderwerp" tonen als `subject` ontbreekt. Optioneel later uitbreiden met fallback naar titel als `subject` ontbreekt maar titel wel aanwezig is.
- Worden gegenereerde inhoudsopgave-slides meegeschreven naar XML?
  - Keuze vastleggen met motivatie (reproduceerbaarheid vs afgeleid gegeven).
  - Keuze: Nee, inhoudsopgave-slides worden dynamisch gegenereerd bij inlezen/tonen, niet opgeslagen.
- Komt `subject` op elke niet-inhoudsopgave-slide in beeld?
  - Ja/Nee + motivatie.
  - Keuze: Ja, onderaan elke slide als onderdeel van meta-informatie.
- Nummering in inhoudsopgave?
  - Ja/Nee + formatteringsregels.
  - Keuze: Ja, nummering gebaseerd op volgorde in slide-sequentie, reset bij nieuw onderwerp.

## Fase 1 - Ubiquitous Language (Opdracht 1) - Check (code, zonder verslag)

3. Ubiquitous Language uitbreiden
- Voeg concepten toe.
- Definieer relaties:
  - Slide heeft 0..1 subject.
  - Inhoudsopgave representeert geordende onderwerpregels uit slide-sequentie.
- Definieer regels:
  - Gelijke opeenvolgende subjects clusteren tot 1 inhoudsopgave-item.
  - Onderwerp opnieuw opnemen na onderbreking door ander onderwerp.

1. Verslaginput voorbereiden voor Opdracht 1
- Neem alleen feature-relevante UL in hoofdtekst.
- Zet oorspronkelijke UL in appendix.
- Beschrijf expliciet welke termen/regels zijn gewijzigd en waarom.

## Fase 2 - Domeinontwerp aanpassen (Opdracht 2) - Check (code, zonder verslag)

5. Domeinmodel herzien op veranderbaarheid
- Voeg expliciet concepten toe voor onderwerp en inhoudsopgave-generatie.
- Zorg dat verantwoordelijkheid voor inhoudsopgave-opbouw niet in UI of infrastructuur zit.

6. Verantwoordelijkheden toewijzen
- Voor elke abstracte klasse/interface in verslag:
  - verantwoordelijkheid,
  - reden van abstractie,
  - afhankelijkheden richting binnenste onion-laag.
- Objectcreatie hier nog niet uitwerken (expliciete opdrachtconstraint).

7. Verslaginput voorbereiden voor Opdracht 2
- Feature-relevante ontwerpdelen in hoofdtekst.
- Oorspronkelijk ontwerp in appendix.
- Duidelijke diff: wat is aangepast en wat bleef gelijk.

## Fase 3 - Architectuur en lagen neerzetten - Check (code, zonder verslag)

8. Onion-architectuur structureren
- Domain laag: entiteiten, value objects, domeinservices, interfaces.
- Application/Services laag: use cases, input/output modellen.
- Infrastructure laag: XML reader/writer en adapters.
- UI laag (Swing/AWT): presentatielogica en event handling.

9. Afhankelijkheden bewaken
- Domein kent geen XML, Swing of concrete infrastructuur.
- Infrastructuur kent domeinservices-contracten of controller (volgens gekozen variant).
- UI praat niet direct met XML parser.

## Fase 4 - XML contract en infrastructuur - Check (code, zonder verslag)

10. DTD uitbreiden
- Voeg toe:
  - `<!ELEMENT subject (#PCDATA)>`
  - `<!ELEMENT toc (#PCDATA)>`
- Verwerk in XML-voorbeeldbestanden.

11. XML inlezen ontwerpen/implementeren
- Infrastructuur leest XML en initieert opbouw van slideshow via serviceslaag.
- Ondersteun:
  - lege slideshow,
  - slide toevoegen,
  - item toevoegen aan laatst toegevoegde slide,
  - subject uitlezen,
  - toc-placeholder herkennen.

12. XML wegschrijven ontwerpen/implementeren
- Serviceslaag levert data; infrastructuur bepaalt XML-representatie.
- Leg beleid vast voor gegenereerde inhoudsopgave-slides bij serialisatie.

## Fase 5 - Feature-implementatie inhoudsopgave - Check (code, zonder verslag)

13. Algoritme voor inhoudsopgave opstellen
- Input: slides met subjects in volgorde.
- Output: onderwerpregels met deduplicatie per aaneengesloten blok.
- Testcases:
  - A,A,A,B,B,A -> A,B,A
  - ontbrekende subjects volgens gekozen fallback.

14. Invoegstrategie voor inhoudsopgave-slides
- Overal waar `toc` staat een gegenereerde slide invoegen.
- Onderwerpen baseren op volledige of context-specifieke scope (keuze documenteren).

15. Stylingregels bepalen
- Basisstijl voor inhoudsopgave-items.
- Eventueel actieve sectie markeren (optioneel).
- Eventueel nummering toepassen.

## Fase 6 - UI en andere lagen aanpassen (Opdracht 3) - Check (code, zonder verslag)

16. Swing/AWT UI integreren
- Minimaal:
  - presentatie openen,
  - navigeren tussen slides,
  - weergave van tekst/image-items,
  - correcte weergave van gegenereerde inhoudsopgave-slide.

17. Communicatie tussen lagen vastleggen
- UI events -> application/service use cases.
- Services leveren view-data terug.
- Infrastructuurtriggers voor inlezen/wegschrijven aansluiten.

18. Verslaginput voorbereiden voor Opdracht 3
- Alleen feature-relevante wijziging in hoofdtekst.
- Oorspronkelijk ontwerp in appendix.

## Fase 7 - Objectcreatie aanpassen (Opdracht 4)

19. Objectcreatie uitbreiden voor nieuwe concepten
- Factories/builders/constructors aanpassen voor subject/toc concepten.
- Zorg dat creatiepad consistent blijft met onion-grenzen.

20. Verslaginput voorbereiden voor Opdracht 4
- Feature-relevante creatie-aanpassingen in hoofdtekst.
- Oorspronkelijk ontwerp in appendix.

## Fase 8 - Patterns documenteren (Opdracht 5)

21. Patterns inventariseren in volledig ontwerp
- Voor elk patroon en elk voorkomen opnemen:
  - naam,
  - klassendiagram,
  - tabel met rollen per klasse/interface,
  - motivatie op basis van GoF intent.

22. Consistentiecontrole patterns
- Controleer dat beschreven intent overeenkomt met daadwerkelijke codeverantwoordelijkheid.

## Fase 9 - Reflectie (Opdracht 6)

23. Individuele reflecties schrijven (ieder apart)
- Voorbereiding oorspronkelijk ontwerp op feature.
- Voorbereiding op toekomstige wijzigingen.
- Ervaring met beginnen vanuit ubiquitous language.
- Ervaring met delen van resultaten.
- Team-samenwerking.

## Fase 10 - Inlevering en kwaliteitscheck (Opdracht 7)

24. Werkend programma valideren
- Eclipse project importeerbaar.
- Run-config werkt op schone omgeving.
- Featuregedrag conform gekozen regels.

25. Eindcontrole verslag en appendix
- Template gevolgd.
- Oorspronkelijke UL/ontwerp in appendix.
- Feature-relevante delen in hoofdtekst.

26. Inleverpakket maken (zip)
- Verslag.
- Sourcecode als Eclipse project.

## Snelle praktische eerstvolgende stappen

1. Maak kopie van oorspronkelijke UL en ontwerp voor appendix.
2. Leg de vier kernkeuzes vast (subject fallback, serialisatie gegenereerde slides, subject tonen op slide, nummering).
3. Zet daarna pas de nieuwe domeinverantwoordelijkheden en interface-grenzen op papier.