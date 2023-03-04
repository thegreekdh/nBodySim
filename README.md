An N-body solar system simulator written in Java. Simulates 23 bodies to the second using the equations of motion and Newton's law of universal gravitation, beginning from the epoch of February 26th, 2023.

Multithreading is implemented but does not result in a significant performance gain given the CPU-intensive workloads involved.

Note that the model is not 100% accurate to reality given that, at higher velocities and in larger gravity wells (e.g. mercury and the sun), relativistic effects begin to creep in. Implementing general relativity was a bit beyond the scope of the project, but may happen someday.