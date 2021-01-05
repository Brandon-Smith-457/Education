using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PerlinNoise
{
    private int seed;
    private System.Random rand;
    private float coarseAmplitude;
    private int coarseWavelength;
    private int xMin;
    private int xMax;
    private int numOctaves;
    private int octaveDivisor;

    private List<Vector3> noiseValues;

    // The object generates a range of noise values between [0, 2] (see sum of (1/2)^i from i = 0 -> inf)
    public PerlinNoise(int seed, float coarseAmplitude, int coarseWavelength, int xMin, int xMax, int numOctaves, int octaveDivisor)
    {
        this.seed = seed;
        rand = new System.Random(this.seed);
        this.coarseAmplitude = coarseAmplitude;
        this.coarseWavelength = coarseWavelength;
        this.xMin = xMin;
        this.xMax = xMax;
        this.numOctaves = numOctaves;
        this.octaveDivisor = octaveDivisor;

        this.noiseValues = generateNoise(this.coarseAmplitude, this.coarseWavelength, this.numOctaves, this.octaveDivisor);
    }

    // Given an x[xMin, xMax] value, for the current seed always return the same y[0, 2] value
    public float getNoise(int x)
    {
        return this.noiseValues[x - xMin].y;
    }

    // Loop over the octaves and for each octave compute a set of size (xMax - xMin) containing the noise values (and the interpolated noise values)
    private List<Vector3> generateNoise(float amplitude, int wavelength, int octaves, int octaveDivisor)
    {
        List<Vector3> results = new List<Vector3>();

        for (int i = 0; i < octaves; i++)
        {
            List<Vector3> octaveResults = computeOctave(amplitude, wavelength);
            if (results.Count == 0) results = octaveResults;
            else if (results.Count == octaveResults.Count)
            {
                for (int j = 0; j < results.Count; j++)
                {
                    results[j] += octaveResults[j];
                }
            }
            amplitude /= octaveDivisor;
            wavelength /= octaveDivisor;
        }

        return results;
    }

    // Compute a single octave's random noise values and the interpolated noise values
    private List<Vector3> computeOctave(float amplitude, int wavelength)
    {
        float y = 0;
        float y1 = (float)rand.NextDouble();
        float y2 = (float)rand.NextDouble();
        List<Vector3> results = new List<Vector3>();
        for (int x = 0; x < this.xMax - this.xMin; x++)
        {
            if (wavelength == 0) wavelength = 1;
            if (x % wavelength == 0)
            {
                y1 = y2;
                y2 = (float)rand.NextDouble();
                y = y1 * amplitude;
            }
            else
            {
                y = TerrainGenerator.instance.cosineInterpolation(y1, y2, (x % wavelength) / wavelength) * amplitude;
            }
            results.Add(new Vector3(x, y, 0));
        }
        return results;
    }
}
