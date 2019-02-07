package arbitraryUnionCalculator;

/* Written by DaWoblefet. The general idea of this class is to represent fractions with
 * large numerators and large denominators. The most important function is
 * calculateArbitraryUnion() which lets the user calculate the union of n sets.
 * Special thanks to DeVon Ingram (dingram) for kick-starting the algorithm idea.*/

import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigFraction
{
	private BigInteger numerator;
	private BigInteger denominator;

	//BigIntegers are created by passing in a String.
	public BigFraction(long numerator, long denominator)
	{
		this.numerator = BigInteger.valueOf(numerator);
		if (denominator == 0)
		{
			throw new ArithmeticException("BigFraction cannot have a 0 denominator");
		}
		this.denominator = BigInteger.valueOf(denominator);
	}

	public BigFraction(String numerator, String denominator)
	{
		this.numerator = new BigInteger(numerator);
		if (denominator.equals("0"))
		{
			throw new ArithmeticException("BigFraction cannot have a 0 denominator");
		}
		this.denominator = new BigInteger(denominator);
	}

	//Decimals -> fractions
	public BigFraction(String decimal)
	{
		String splitDecimal[] = decimal.split("\\.");
		int decimalLength = splitDecimal[1].length();

		this.denominator = BigInteger.TEN.pow(decimalLength);
		this.numerator = (new BigDecimal(decimal).multiply(new BigDecimal(this.denominator))).toBigInteger();
	}

	//Default fraction is 0/1
	public BigFraction()
	{
		this.numerator = BigInteger.ZERO;
		this.denominator = BigInteger.ONE;
	}

	public BigInteger getNumerator()
	{
		return this.numerator;
	}

	public BigInteger getDenominator()
	{
		return this.denominator;
	}

	public void setNumerator(BigInteger numerator)
	{
		this.numerator = numerator;
	}

	public void setDenominator(BigInteger denominator)
	{
		this.denominator = denominator;
	}

	public BigFraction add(BigFraction f2)
	{
		BigFraction result = new BigFraction();

		BigInteger f1Numer = this.numerator;
		BigInteger f1Denom = this.denominator;
		BigInteger f2Denom = f2.getDenominator();

		if (f1Denom.equals(f2Denom)) //if the denominators are the same, no need to find the LCM
		{
			result.setNumerator(f1Numer.add(f2.getNumerator()));
			result.setDenominator(f1Denom);
		}
		else //Find the LCM and scale the fractions appropriately.
		{
			BigInteger lcm = findLCM(f1Denom, f2Denom);
			result.setDenominator(lcm);
			result.setNumerator(((lcm.divide(f1Denom)).multiply(f1Numer)).add(((lcm.divide(f2Denom)).multiply(f2.getNumerator()))));
		}
		return result;
	}

	public BigFraction subtract(BigFraction f2)
	{
		BigFraction result = new BigFraction();

		BigInteger f1Numer = this.numerator;
		BigInteger f1Denom = this.denominator;
		BigInteger f2Denom = f2.getDenominator();

		if (f1Denom.equals(f2Denom)) //if the denominators are the same, no need to find the LCM
		{
			result.setNumerator(f1Numer.subtract(f2.getNumerator()));
			result.setDenominator(f1Denom);
		}
		else //Find the LCM and scale the fractions appropriately.
		{
			BigInteger lcm = findLCM(f1Denom, f2Denom);
			result.setDenominator(lcm);
			result.setNumerator(((lcm.divide(f1Denom)).multiply(f1Numer)).subtract(((lcm.divide(f2Denom)).multiply(f2.getNumerator()))));
		}
		return result;
	}

	public BigFraction multiply(BigFraction f2)
	{
		BigFraction result = new BigFraction();

		result.setNumerator(this.numerator.multiply(f2.getNumerator()));
		result.setDenominator(this.denominator.multiply(f2.getDenominator()));

		return result;
	}

	//An intersect between fractions is just multiplication.
	public BigFraction intersect(BigFraction f2)
	{
		return this.multiply(f2);
	}

	public String toDecimal()
	{
		String result = "";
		try
		{
			BigDecimal decimal = new BigDecimal(this.numerator).divide(new BigDecimal(this.denominator));
			result = decimal.toPlainString();
		}
		catch (ArithmeticException e) //BigDecimal can't represent a non-terminating decimal. See: https://docs.oracle.com/javase/7/docs/api/java/math/BigDecimal.html#divide(java.math.BigDecimal)
		{
			result = "Approximate: ";
			result += new BigDecimal(this.numerator).divide(new BigDecimal(this.denominator), 5, RoundingMode.CEILING).toString();
		}

		return result;
	}

	public String toPercent()
	{
		String result = "";
		try
		{
			BigDecimal decimal = (new BigDecimal(this.numerator).divide(new BigDecimal(this.denominator)).multiply(new BigDecimal("100")));
			decimal = decimal.stripTrailingZeros();
			result = decimal.toPlainString() + "%";
		}
		catch (ArithmeticException e) //BigDecimal can't represent a non-terminating decimal. See: https://docs.oracle.com/javase/7/docs/api/java/math/BigDecimal.html#divide(java.math.BigDecimal)
		{
			result = "Approximate: ";
			BigDecimal decimal = new BigDecimal(this.numerator).divide(new BigDecimal(this.denominator), 7, RoundingMode.CEILING).multiply(new BigDecimal("100"));
			decimal = decimal.stripTrailingZeros();
			result += decimal.toPlainString() + "%";
		}

		return result;
	}

	//To reduce a fraction, divide the numerator and denominator by the GCD.
	public BigFraction reduce()
	{
		BigFraction result = new BigFraction();
		BigInteger gcd = this.numerator.gcd(this.denominator);
		result.setNumerator(this.numerator.divide(gcd));
		result.setDenominator(this.denominator.divide(gcd));
		return result;
	}

	//To find LCM, a*b/(gcd(a,b))
	public BigInteger findLCM(BigInteger a, BigInteger b)
	{
		return (a.multiply(b)).divide(a.gcd(b));
	}

	@Override
	public String toString()
	{
		return this.numerator + "/" + this.denominator;
	}

	public static BigFraction calculateArbitraryUnion(BigFraction[] events)
	{
		BigFraction result = new BigFraction();

		//Start by reducing the fractions to hopefully save computation time.
		for (int i = 0; i < events.length; i++)
		{
			//If one of the events has probability 1 or higher, then the union is guaranteed to be 1.
			if (events[i].getNumerator().compareTo(events[i].getDenominator()) >=0)
			{
				result = new BigFraction(1,1);
				return result;
			}
			events[i] = events[i].reduce();
		}

		//The total number of subsets for an n-set union is always 2^n.
		int[][] subsets = new int[(int)Math.pow(2,events.length)][events.length];

		//Initializes the contents of subsets to be -1 for comparison later.
		for (int i = 0; i < subsets.length; i++)
		{
			for (int j = 0; j < events.length; j++)
			{
				subsets[i][j] = -1;
			}
		}
		int currentSubset = 0;
		int currentSubsetMember = 0;

		//Find all subsets of the set of events. Modified from: https://www.geeksforgeeks.org/finding-all-subsets-of-a-given-set-in-java/
		for (int i = 0; i < (1 << events.length); i++)
		{
			for (int j = 0; j < events.length; j++)
			{
				if ((i & (1 << j)) > 0)
				{
					subsets[currentSubset][currentSubsetMember] = j;
					currentSubsetMember++;
				}
			}
			currentSubset++;
			currentSubsetMember = 0;
		}

		BigFraction tempResult;
		boolean addIntersection;

		//Now loop through the subsets and intersect all elements in each subset, then determine to add/subtract.
		//Skip the first subset (skip 0, so start at 1), because it's always the empty set.
		for (int i = 1; i < subsets.length; i++)
		{
			tempResult = new BigFraction(1,1);
			addIntersection = true;

			for (int j = 0; j < subsets[i].length; j++)
			{
				if (subsets[i][j] == -1) //If it hits a -1, you're done with this subset and decide to add/subtract.
				{
					if (j % 2 != 0)
					{
						addIntersection = true;
					}
					else
					{
						addIntersection = false;
					}
					break;
				}
				if (j == (subsets[i].length - 1)) //If it's the last element in the subset, decide to add/subtract.
				{
					if (j % 2 == 0)
					{
						addIntersection = true;
					}
					else
					{
						addIntersection = false;
					}
				}
				tempResult = tempResult.intersect(events[subsets[i][j]]);
			}

			//If the number of elements in a subset is odd, add; if it is even, subtract.
			if (addIntersection)
			{
				//System.out.println("Adding " + result + " and " + tempResult + ".");
				result = result.add(tempResult);
			}
			else
			{
				//System.out.println("Subtracting " + result + " and " + tempResult + ".");
				result = result.subtract(tempResult);
			}
			//System.out.println("Outcome: " + result);
		}

		return result;
	}
}