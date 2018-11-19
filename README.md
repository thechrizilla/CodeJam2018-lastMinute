# Comic Translator
Program to auto-translate English comics to a desired language, effortlessly increasing accessibility to audiences. Made as a submission for the CodeJam 2018 Pattern Recognition Hackathon.

## Inspiration
We enjoy comics, we speak different languages. Not all comics are available in every language. We aim to make them accessible to everyone, so everyone can enjoy their favourite stories and characters without the barrier of language.

## What it does
Given an image of a comic book page, the program will recognize all English dialogue text and replace it with the translated text in the desired language.

## How we built it
We coded everything in Java since it was the only language we all knew (somewhat) knew in common. The process starts with finding speech bubbles and dialogue boxes on the page using OpenCV. Then, using Tess4J, a public Java JNA wrapper for the Tesseract OCR API, text is extracted from the image by specifying the region to perform the OCR, using the contour data of the dialogue elements found in the last step. Using the previously obtained contour data, speech and dialogue boxes are filled with the appropriate background colour to empty out speech bubbles. A JSON Java library is used to access the Google Translate API to translate the extracted text, and it is simply a matter of drawing the translated text back onto the modified image.

## Challenges we ran into
No one in the group has any experience with Pattern Recognition, which proved to be a huge obstacle to us in both brainstorming and development, as we had to learn completely new concepts and apply them in such a short span of time. One specific challenge for us was getting the OCR to distinguish text from the rest of the non-text visuals in an image, which is somewhat difficult to do in the context of visual novel mediums. Additionally, half of the team are in their first semester at McGill and haven't had much experience in programming/software development. Further, no one in the team has been in a hackathon before, and so figuring out how to navigate the hackathon itself was also a challenge!

## Accomplishments that we're proud of
It is everyone's first time doing a hackathon and we are proud that, in such a short span of time, we were able to learn and use technologies completely new to us in a field we've never stepped foot in (despite mostly using public APIs), working together in a team fluidly to create a working, practical prototype that is both relevant to our interests and helps others in such a short span of time.

## What we learned
We learned many things, both technically and experience-wise. In general, we learned about computer vision, a topic we had no experience in, and how complex but powerful this technology is, learning how to use public APIs and libraries like OpenCV and Tess4J. We gained a ton experience in software development, creating a full program. But most importantly, we gained valuable experience, learning how to work closely as a team, adapting to each other and communicating well to push out a product in a timely manner.

## What's next for Comic Translator
We would like to expand the idea to make it more general and broad purpose. Goals in the future include recognizing other languages in the source image using other OCR technologies, not limiting us to only translating English comics to other languages but allowing effortless translation of visual graphic novel media from and language to any language, truly making any comic in any language accessible to others. We would also like to add quality of life features like being able to translate more than one page at a time, translating whole chapters at once. Improved pattern recognition techniques would allow us to recognize text in coloured dialogue bubbles and remove them appropriately.

Moreover, comic book panel text bubbles have too much noise around it, making it very hard for our program to be able to detect where the text bubbles are. We would like to implement a noise reduction method that increases contrast of the text bubble. 

Furthermore, our program currently accesses Google translate through an URL, and gives the URL an input, and gets an output from the URL. We would like to have a better free translator API that will be able to translate the text bubbles that is syntactically, and grammatically correct.  
