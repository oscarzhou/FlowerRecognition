# Flower Recognition

## Introduction  

The original idea about this project is to help tourists study and know the name, the genre and even the story of certain plant they never saw before. In fact, it is the problem that I met when I came to New Zealand first time. The plants here are totally different from those in my country and I am curious about them. Thus, this is where the project comes into the picture.

## Technology

This is a personal project coded by Java, developed in the AndroidStudio. The core of the project is calling **Google Vision API** to implement the **image recognition**. In fact, the functionality of the application is not just limited in the Plant Recognition, it can recognize all the stuff because of the power of the **Google Vision API**.  

There are three main components:

### 1.Take a photo  
Description: You will get the a bunch of results with the percentage of likelihood after taking a picture. In the part, I have done some processing on this result set by filtering some key words, such as color words, general and basic words, making sure the result more specific.  

### 2.Select a picture from the gallery  
Description: You are allowed to access your gallery to select a picture to analyze. The purpose of this function is to make it possible that user still can study the species after going back to home. Because it is quite common that the place you find certain species is where no network service is provided, so you can't use this application at that time.  

### 3.Browse the dictionary of the plants  
Description: This function is mainly used for studying. A list with the picture and title of the species is in this component. You are allowed to click the item to redirect a website page that includes more relevant information. The information list is obtained by a web crawler program developed by python.  

## The part where can be improved

1. Add the use of the database  
2. Increase the numbers of the key words that can be filtered  
3. Integrate the web crawler into the project and update data automatically in the background  

