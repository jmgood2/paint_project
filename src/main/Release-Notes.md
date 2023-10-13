GitHub: https://github.com/jmgood2/paint_project.git

Video:

Pain(T) 2.0 Release notes

# 10/13/2023 Release Notes (Alpha Build 2.3.1)
> ---   
>
>
>
>### News
>
> Heavy Update. Added much functionality, listen in Overview. We now have a variety of 
> functions for shapes and a color picker. Some canvas work has been done, though we still
> dont have a resized canvas. Fixed a LOT of known issues. Migrated a lot of the functionality
> to the imagehandler. Main file is now Painter.java. Also introduced temp images for
> redo and undo functionality. Also added a smart save warning upon closing an unsaved image.
>
> ### Overview
>	- #### New Features
>>  - Shapes
>>  - Color Picker
>>  - Undo/Redo 
>>  - Line/Shape Outline/Dashes
>>  - Scrolling for canvas
>>  - Color Labeling
>>  - Expanded Key Shortcuts
>>  - Line width controls (manual entry and slider)
>    - Migrated methods to other Handlers
>    - Modified images now saved as temp files in a temporary folder
>    - Undoing and then modifying an image now deletes all previous temp images earlier in the stack
>    - Fixed some wonkiness with drawing lines and shapes
>    - Added new contextual panes for the various draw options
>    - Added some minimal Logging
> -  #### Known Issues
>>    - Left pane has variable width
>>    - Dash selection for lines does not reset upon switching between Free and Line drawing
>>    - Dash selection menu does not appear on Line context pane
>>    - Still some issues with Saving - no default image type
>>    - Ends of free drawn lines are very... square
>
>
>---
>## Coming Soon
>
> #### User Facing
> - New Shape creation (Hexagon?)
> - Better editing tools
>> - Eraser tool
>> - Selection
>> - Rotation
>> - Movement
>> - Copy
>> - Paste
> - Tabs for multiple canvases
> - Text Functionality
> - New image Functionality
>
>
>
> #### Back-End
> - Incorporate Threading and more Logging
>

# 9/15/2023 Release Notes (Alpha Build 2.1.1)
> ---   
>
>
>
>### News
>
> This is an expansion on the last 2.1 
> build. I have finally finished getting 
> SwingFXUtils to works, which has been a real bit of work.
> I've had to completely remove my IDE and reinstall, along
> with reworking my Javafx libraries. A lot was added from the initial 
> PainT project 1.2 to save time in the future. 
> 
> There are still some bugs with open/save involving .bmp files, as well
> as some stuttering on free-draw. Line width also needs re-implementation, as
> does the general layout of palette sidebar.
> 
> Next up is refining the backend and maybe implementing some editing options.
> 
> ### Overview
>	- New Features
>    - Saving images
>    - Drawing with Line or Free hand
>    - Palette of colors
>    - Line width selection
> -  #### Known Issues
>   - Canvas not resizing to fit massive images
>   - Palette menu being moved by massive images
>   - Save dialog box echoing
>   - .bmp not loading/saving correctly
>   - .tiff might not be, I'd have to find a .tiff image to test
>   - There is an extra bullet point
>   - 
> 
>
>---
>## Coming Soon
>
> #### User Facing
> - Better UI
> - Editing options
> - image scrolling
> - increased palette and draw size
>
> 
> 
> #### Back-End
> - Migrating primary project method to its own class
> - Temp file handling
>


# 9/9/2023 Release Notes (Alpha Build 2.1)
> ---   
>
>
>
>### News
>
> This is a rework of the original Pain(T) project (found here https://github.com/jmgood2/paint_1.git). This new alpha build is rebuilt from the ground up and is based on that initial build.
>
>
> ### Overview
>	- Software can
       - open an image and display it
       - Rudimentary controls (save, save as, close)
       - Menubar
       >    - Help menu with ABOUT information and link to these release notes.
>   - Shortcuts for Opening/Closing Images as well as Save/Save as. Also shortcut for exiting program
>
>
>---
>## Coming Soon
>#### User-Facing
> - Editing Options
    - Drawing (line)
    - Line color and width options
> -
> -
>
> #### Back-End
>- Logging - Introduce logging for this program
>
> ---

