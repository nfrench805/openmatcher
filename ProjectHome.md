# goals #
  * encode image to extract caracteristics points
  * match 2 images (encoded) and get a Hit score

System is developped in Java 1.6.

Inspired by Phase Correlation Theory, the algorithm consists to:
  * compute **FFT** of each image
  * compute **phase correlation** (_Phase Only Correlation_)
  * compute greater peaks and finally evaluate similarities score (matching score) and    rotation, translation, scale differences between both images.

A log polar coordinates will be used to estimate rotation angle.