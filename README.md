# Arbitrary Union Calculator
![Screenshot](https://i.imgur.com/jQXf3Ba.png)

The arbitrary union calculator performs a union on an arbitrary number of events (as probabilities) and displays the result as a decimal and fraction. This has application with probability management in Pokemon. That is, it finds

![Equation](https://i.imgur.com/oEdIqdF.png)


What is a Union?
-----------

A union is the probability of some event A OR some event B occurring. For example, suppose in order to win the game, you need to flinch the opponent's Pokémon with Iron Head (3/10) or get a critical hit with Iron Head (1/24). The target is in KO range of a critical hit Iron Head, but it will never faint to an Iron Head otherwise. In order to determine what this probability is, you take the chance to flinch and add it to the chance to land a critical hit. In this case, 3/10+1/24. However, we want to make sure we don't "double-count", if you will. We really don't care about getting a flinch AND a critical hit; in this case, that'd be impossible, as a critical hit would just KO the target, so there's no need for a flinch. As a result, we have to subtract the combined chance of a crit AND a flinch happening; that is, 3/10 * 1/24. Our final result is:

3/10+1/24-(3/10 * 1/24) = 79/240, which is about 32.9%. This should make some sense intuitively; if you can get a 30% flinch or a 1/24 critical hit, the odds of getting either/or should be higher than 30% at least.

In addition to OR, there is also AND. You use AND when you want to figure out the chance of event A AND event B happening. For example, what are the odds of the opponent flinching both of your Pokémon with Rock Slide (that is, flinch AND flinch)? Let's check it out.
Odds of hitting Rock Slide: 90% = 9/10
Odds of getting a flinch: 30% = 3/10

So the opponent must connect both Rock Slides, and they must flinch both targets. Because all of these conditions are required, we would AND these all together. That is, 3/10 AND 9/10 AND 3/10 AND 9/10. Fortunately, evaluating AND is easy – it's literally just multiplication. 3/10 * 9/10 * 3/10 * 9/10 = 729/10000, a lowly 7.29%.

Mathematicians use notation to help generalize this somewhat. The OR, or union of events, is represented with the "∪" symbol. If I wrote A ∪ B, you would say that as "A OR B" or "A union B" out loud. The AND, or intersection of events, is represented with the "∩" symbol. Similarly, if I wrote A n B, you'd say "A AND B" or "A intersect B" out loud. You may also see things like P(A) or P(A ∪ B) or similar things; this is just shorthand to say "the probability of A", etc.

So, this didn't seem too bad, right? Well, the problem of "double-counting" exists and is amplified if you have three or more events. Here's the "formula", if you will, for the union of two events:

`P(A ∪ B) = P(A) + P(B) - P(A∩B).`

Here's what it looks like for three events:

`P(A ∪ B ∪ C) = P(A) + P(B) + P(C) – P(A∩B) - P(A∩C) - P(B∩C) + P(A∩B∩C).`

And for four events:

`P(A ∪ B ∪ C ∪ D) = P(A) + P(B) + P(C) + P(D) - P(A∩B) - P(A∩C) - P(A∩D) - P(B∩C) - P(B∩D) - P(C∩D) + P(A∩B∩C) + P(A∩B∩D) + P(A∩C∩D) + P(B∩C∩D) - P(A∩B∩C∩D).`

As you might guess, the complexity continues to increase as you add more events. While there are certainly patterns, it becomes tedious and prone to human error to do this manually, both by hand and by via manual input with a calculator. That's why you should use the Arbitrary Union Calculator instead! If you're interested in the mathematics, I would suggest further reading here:

- https://math.stackexchange.com/questions/94853/probability-of-a-union/94924#94924
- https://www.thoughtco.com/probability-union-of-three-sets-more-3126263
- https://en.wikipedia.org/wiki/Union_(set_theory)

The basic logic of my program is as follows:
  1)  Receive all events from the user and treat them as a set.
  2)  Find all subsets of that set.
  3)  Perform an intersection of all events of some subset.
  4)  If the number of events in the subset was odd, add. Else, subtract.
  5)  Continue until all subsets are considered and return the answer.
