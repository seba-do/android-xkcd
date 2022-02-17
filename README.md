# Recap Exercise

## Description

Use all the stuff we learned until now to build an App to show and save comics from XKCD.

## Requirements

- App with one activity and multiple fragments
- First fragment 
    - should show the newest comic
    - should have navigation buttons (next, previous and random)
        - next shows a newer comic
        - previous shows an older comic
        - random shows any comic from the api
    - should have a like button
- Second fragment
    - should show a list of all liked comics
    - possibility to remove a liked comic
- Persistence by using shared-preferences
- API https://xkcd.com/json.html
- Data class to represent the response
- Bonus: Add an init screen with a logo
- Bonus: Take care of configuration changes

## Technologies

- Activities / Fragments
- RecyclerView
- SharedPreferences
- Retrofit
- Glide / Picasso / Coil

## Part 2

- Add and use Room to store the data
