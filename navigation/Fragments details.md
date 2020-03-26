# Fragments UI elements
In this document we will discuss and describe what information each fragment should expose to the UI. A fragment represents a destination in the navigation graph [nav_graph_link] therefore the elements in its UI should reflect clearly the possible paths shown in the graph. We will also identity possible Android components which can fulfill the representation requirements of the fragment.

 There are Android components which repeat thoughout the fragments, those being:
 -  [recycler view](https://developer.android.com/guide/topics/ui/layout/recyclerview), the recommended Android Component for a list

## Home Fragment
The home fragment is the starting point of the application and should allow the user to navigate to the rest of the app.  It should contain:
 
 - Courses Button (takes the user to the curricular terms fragment)
 - Calendar Button 


## Curricular terms Fragment
A programme has multiple calendar terms (usually 6), which means a list will probably be the most appropriate.  The elements should be buttons and they can be represented like:

 - 1ºAno - 1ºSemestre
 - 1ºAno - 2ºSemestre
 - 2ºAno - 3ºSemestre
 - ....

or maybe separate in two fragments, which may lead to too many clicks for the user.

**First fragment:**
- 1ºAno
- 2ºAno
- 3ºAno

**Second fragment:**
for the first year:
 - 1ºSemestre
 - 2ºSemestre
 
for the second year:
 - 3ºSemestre
 - 4ºSemestre
...

## Courses Fragment
A  curricular term has multiple courses, some mandatory and others optional. We have three possibilities: 

 1. A List with all courses, opcional and mandatory;
 2. Two buttons, mandatory and optional, which when clicked lead to a course list;
 3. A list with all mandatory and an item with a button which travels to the optional courses;

Each element in the list could be:

  - Course Full Name (Course Acronym)  
 e.g Programming (PG)
 - Course Full Name 
eg : Programming

## Course Details Fragment
The fragment should contain:
 - Course full name
 - Course acronym
 - Coordenator
 - FUC or link to FUC
 - Ects
 - Hours of Work
 - Classes (UI element to travel to Classes fragment)
 - Link to more details (isel.com link)

Note : This representation should be revised when the core publishes the final version of the read API.

## Classes Fragment
This fragment will present the classes of a course with focus on the ones which are from the current calendar term. The classes will be in a list, and the user can filter them by selecting a diferent calendar term from a drop down list.
A list element should have:

 - Class title (e.g LI52D/LI62D)

Drop down list:

 - Calendar term identifier (e.g 1819v)

And:

 - The course name

The dropdown list can be a [spinner](https://developer.android.com/guide/topics/ui/controls/spinner)

## Class Section Fragment

This fragment should contain:
 
 - Classe identifier
 - Lecturer
 - Course Name
 - Schedule
 - Events (a button to travel to the another fragment)
 - A heart/star to add the class section to favorites/selected
 
eventually:

- Student groups
- Files published by the teachers
- News (which can lead to notifications)

## Favorites 
This fragment contains a list with the selected class sections. Each item should allow the user to go to the class section details fragment. The user should be able to remove items from the list. 

List item details:
 
  - Course Name
  - Calendar Term
  - Class
  
e.g DAW-1819v-LI61D


Note : Maybe an add class section button could be useful
