#include<stdio.h>
#include<stdlib.h>
#include<math.h>
#include<string.h>
#include<unistd.h>  // For usleep() function

int main() {  
    
    float A = 0, B = 0;  // A and B represent rotation angles along different axes.

    float i, j;  // Loop control variables for iterating through the surface of the 3D object.
    
    int k;  // Loop control variable for printing the frame.
    
    float z[1760];  // Array to store depth values (z-buffer) for pixels (used for hidden surface removal).
    
    char b[1760];  // Array to store the ASCII characters representing the pixels of the object.
    
    printf("\x1b[2J");  // ANSI escape code to clear the screen at the beginning.

    // Infinite loop to keep rendering the rotating object.
    for(;;) {
        
        // Initialize the buffers for each frame:
        // b[] is filled with space characters (ASCII 32) to represent empty pixels.
        // z[] is filled with zeros to reset the depth buffer for the new frame.
        memset(b, 32, 1760);  
        memset(z, 0, 7040);  
        b[1760] = '\0';  // Null-terminate the character array to treat it as a string.
        
        // Double loop to calculate the surface points of the rotating object.
        // j and i represent angles around the object in two dimensions (akin to parametric equations).
        for(j = 0; j < 6.28; j += 0.07) {  // Outer loop: Iterate over the "vertical" angle (around the Y-axis).
            
            for(i = 0; i < 6.28; i += 0.02) {  // Inner loop: Iterate over the "horizontal" angle (around the X-axis).
                
                // Trigonometric calculations for the 3D coordinates of the point on the object's surface.
                float c = sin(i);  // Compute sine of angle i.
                float d = cos(j);  // Compute cosine of angle j.
                float e = sin(A);  // Sine of the rotation angle A (affects Y-axis rotation).
                float f = sin(j);  // Sine of angle j.
                float g = cos(A);  // Cosine of the rotation angle A.
                float h = d + 2;   // A helper value to translate the object on the Z-axis.

                // Perspective projection to calculate how far the point is from the camera.
                float D = 1 / (c * h * e + f * g + 5);  // D is the depth (inverse of Z-coordinate).
                float l = cos(i);  // Cosine of angle i (affects the X-axis position).
                float m = cos(B);  // Cosine of the rotation angle B (affects X-axis rotation).
                float n = sin(B);  // Sine of the rotation angle B (affects X-axis rotation).
                float t = c * h * g - f * e;  // A transformation term for rotating the object.
                
                // Map the 3D coordinates to 2D screen coordinates.
                int x = 40 + 30 * D * (l * h * m - t * n);  // X-coordinate on screen.
                int y = 12 + 15 * D * (l * h * n + t * m);  // Y-coordinate on screen.
                int o = x + 80 * y;  // Compute the 1D index for the screen buffer using the 2D coordinates.
                
                // Calculate a "brightness" value (N) for the point based on its orientation.
                int N = 8 * ((f * e - c * d * g) * m - c * d * e - f * g - l * d * n);

                // Check if the point lies within the screen bounds and is closer than previous points (z-buffer check).
                if (o >= 0 && o < 1760 && 22 > y && y > 0 && x > 0 && 80 > x && D > z[o]) {
                    z[o] = D;  // Update depth buffer with the new depth value.
                    N = (N > 0 && N < 12) ? N : 0;  // Clamp N to ensure it's within the range of the ASCII characters.
                    b[o] = ".,-~:;=!*#$@"[N];  // Set the brightness of the point by choosing an ASCII character.
                }
            }
        }

        // Print the frame to the terminal:
        printf("\x1b[H");  // ANSI escape code to move the cursor to the top left of the screen.

        // Loop through the screen buffer and print each character:
        for(k = 0; k < 1761; k++) {
            putchar(k % 80 ? b[k] : 10);  // Print each character, inserting a newline after every 80 characters.
            A += 0.00004;  // Slightly increment the rotation angle A for smooth rotation (Y-axis rotation).
            B += 0.00002;  // Slightly increment the rotation angle B for smooth rotation (X-axis rotation).
        }
        
        usleep(30000);  // Pause for 30 milliseconds to control the animation speed.
    
    }
    return 0;
}

// Made By Thy Regentos
