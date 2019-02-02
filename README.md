# Artificial Image Generator
This repository contains the source code for a Java program called ArtificialImageGenerator, which was developed to reproduce a MatLab routine previously described by Sbalzarini and co-workers (Sbalzarini and Koumotsakos, 2005). 

## Method Description
The generator produces:  
1. Images with variable bit depth (8-bit / 16-bit / etc.).
2. Images with variable pixel height and width.
3. Variable numbers of images per dataset.
4. Variable numbers of image frames per image.
5. Variable frame intervals.
6. Variable numbers of bright spots per image frames.
7. Variable bright spots mobility between frames.
8. Variable spot pixel radiuses. 

In brief:
1. In order to model different image qualities (vis. Signal-To-Noise-Ratios), image frames can be initialized with variable background intensity values (B), and bright spots can be assigned variable peak intensities (PI).
2. Microscopic observation is simulated by sampling a Gaussian with a given σ value, µ = PI, and centered at each known bright spot position.
3. In order to model Poisson-distributed Shot noise associated with CCD camera image acquisition, the intensity value (I) at each pixel was replaced with a number sampled at random from a Poisson distribution of mean value µ = I. The random seed is reinitialized for each image.

## Method Validation
The functionality of the Java program was evaluated by direct comparison with the MatLab version of the algorithm. 
For validation purposes, artificial image planes were directly compared (i.e., pixel-per-pixel) to those generated using the published algorithm (Sbalzarini and Koumotsakos, 2005). Discrepancies measured either as the number of discordant pixels, or as the distance from ground-truth of the observed mean intensities over the entire image, were very rare and consistent to numerical errors due to well known rounding differences between MatLab and Java and possibly due to the use of different hardware (Rigano et al., 2018a and 2018b).

## References
1. Sbalzarini, I.F., and P. Koumoutsakos. 2005. Feature point tracking and trajectory analysis for video imaging in cell biology. J Struct Biol. 151:182–195. https://doi.org/10.1016/j.jsb.2005.06.002.
2. Rigano, A., V. Galli, K. Gonciarz, I.F. Sbalzarini, and C. Strambio-De-Castillia. 2018a. An algorithm-centric Monte Carlo method to empirically quantify motion type estimation uncertainty in single-particle tracking. bioRxiv. 379255. https://doi.org/10.1101/379255.
3. Rigano, A., V. Galli, J.M. Clark, L.E. Pereira, L. Grossi, J. Luban, R. Giulietti, T. Leidi, E. Hunter, M. Valle, I.F. Sbalzarini, and C. Strambio-De-Castillia. 2018b. OMEGA: a software tool for the management, analysis, and dissemination of intracellular trafficking data that incorporates motion type classification and quality control. bioRxiv. 251850. https://doi.org/10.1101/251850.
