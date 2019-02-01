# Artificial Image Generator
## Method Description
This repository contains the source code for a Java program called ArtificialImageGenerator, which was developed to reproduce a MatLab routine previously described by Sbalzarini and co-workers (Sbalzarini and Koumotsakos, 2005). 
In brief:
1. Image sets each containing 1000 independently generated synthetic planes are created to contain simulated point sources.
2. Planes are initialized by adding a background (black) intensity value of B = 10 to each pixel. 
3. In order to model different image qualities (vis. SNR), point sources are assigned variable intensity values (I).
4. In order to model microscopic observation is simulated by sampling a Gaussian of σ = 1 pixel and µ = I, centered at each known point position. 
5. In order to model Poisson-distributed Shot noise associated with CCD camera image acquisition, I was replaced at each pixel with a number sampled at random from a Poisson distribution of mean value µ = I. 
All random numbers are generated independently for every trial and plane. Artificial planes are stored as unscaled 16-bit TIFF files. 

## Method Validation
The functionality of the Java program was evaluated by direct comparison with the MatLab version of the algorithm. 
For validation purposes, artificial image planes were directly compared (i.e., pixel-per-pixel) to those generated using the published algorithm (Sbalzarini and Koumotsakos, 2005). Discrepancies measured either as the number of discordant pixels, or as the distance from ground-truth of the observed mean intensities over the entire image, were very rare and consistent to numerical errors due to well known rounding differences between MatLab and Java and possibly due to the use of different hardware (Rigano et al., 2018a and 2018b).

## References
1. Sbalzarini, I.F., and P. Koumoutsakos. 2005. Feature point tracking and trajectory analysis for video imaging in cell biology. J Struct Biol. 151:182–195. doi:10.1016/j.jsb.2005.06.002.
2. Rigano, A., V. Galli, K. Gonciarz, I.F. Sbalzarini, and C. Strambio-De-Castillia. 2018a. An algorithm-centric Monte Carlo method to empirically quantify motion type estimation uncertainty in single-particle tracking. bioRxiv. 379255. doi:10.1101/379255.
3. Rigano, A., V. Galli, J.M. Clark, L.E. Pereira, L. Grossi, J. Luban, R. Giulietti, T. Leidi, E. Hunter, M. Valle, I.F. Sbalzarini, and C. Strambio-De-Castillia. 2018b. OMEGA: a software tool for the management, analysis, and dissemination of intracellular trafficking data that incorporates motion type classification and quality control. bioRxiv. 251850. doi:10.1101/251850.
