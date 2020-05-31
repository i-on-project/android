# Capabilities determined by Json Home

Json Home is the root resource of the i-on core Web API. It provides the starting point of the navigation capabilities within the Web API. This implies that if a resource is not available in root then we cannot access it and navigate to the resources which are children of this resource. Consequently we have to take actions dependent on the availability a resource. This may be the removal of functionalities of simply shutting down the application.

The resources which root provides are:
 - Programmes
 - Calendar Terms
 - Courses

 ## Programmes
This resource is the base of the android application,  it provides navigation to all the most important resources. 

Navigation path:

Programmes -> ProgrammeDetails -> ProgrammeOffers -> CourseDetails -> Classes -> ClassDetails -> Events


## Calendar Terms
This resource provides the list of calendar terms (1819i, 1819v, 1920i, 1920v ...). Currently we use this in two areas of the application, in favorites, so that the user can filter its favorites according to the calendar term, and in course details, so that the user can obtain the classes of a specific calendar term. 

Navigation path:
CalendarTerms -> Classes -> ClassDetails -> Events

## Courses
This resource represents the list of all courses. And it also provides navigation to details of a course.
We are currently only using it to access the details of a course. 

Navigation path:
Courses -> CourseDetails -> Classes -> ClassDetails -> Events






