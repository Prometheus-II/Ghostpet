>Quickstart Guide
	Welcome to your new desktop pet. To start with, I'm assuming that you've downloaded a .zip file containing this, unzipped everything into its own folder, and opened this readme file to see what to do next. If so...congratulations, that's basically it. Really! Just run the ghostpet.jar file (or use the handy little shortcut named Ghostpet, which does exactly that but can be moved to your desktop or wherever you want it without breaking everything) and you'll see your little ghost pop up in the corner of your screen.

	Now, the problem with this is that the default ghost is...honestly kinda bad. It's just a little stick figure. However, this is easy to fix, even if you know absolutely nothing about programming! Now, if you are in fact completely inexperienced, I'd suggest ALSO downloading the Ghost Designer application, which has its own handy little guide to step you through and which gives you a good deal of editing freedom, although you should still read this guide first so you know what I'm referring to in that one. But if you just want to make a few simple changes, I'll step you through that here. 
	
>How to Make Basic Changes
	In the folder you unzipped your ghost into, there should be a folder called "resources." In it should be a bunch of .json files and a folder named "images." The images folder contains a bunch of .png images - at the time of downloading, probably just a few stick figures. This is where you'll put the images you want your ghost to use for its appearance. (It's important that any image you use has a blank background, NOT just a white one. Otherwise you won't be able to click the area around it, and that would be bad.) You'll refer to these images by name when changing how the ghost acts, so feel free to give them any name you want. 
	
	As for the .json files, you'll notice that one of them is named GhostJson.json, and the rest are all named [something]Behavior. These are what tell the ghost how to look and what to do, and you can edit them with any text editor you have (I recommend Notepad++). I'll go over the Behavior files a little bit later, but they're what dictate the particulars of how your ghost acts - what it says, how long it says it for, what it looks like while it does, and so on. The GhostJson file is all the defaults, so I'll explain that first.
	
	(There's also a .ttf file in the resources folder. You can replace it with whatever other .ttf file you want to change the font your ghost uses, but it's not necessary.)

>Anatomy of the GhostJson File
	The file is pretty simple, consisting of five entries.
	* ghostName is how the ghost refers to itself. In the Behavior files, you'll notice places where the "word" $GNAME$ gets used. When the ghost actually "says" those statements, $GNAME$ will be replaced by whatever text is under this entry.
	* UserName is basically the same, except it replaces $NAME$. It's how the ghost refers to you. Pick something fun.
	* DefaultImg is the name of the image the ghost uses by default (so exactly what it sounds like). Pick an image from the folder, and the ghost will display that all the time.
	* TxtBoxImg is the name of the image the ghost uses as a background for its lines, because black text on a background of nothing is really hard to read. Again, just pick an image from the folder to replace its name with.
	* FontFile is the .ttf file holding the font you want your ghost to speak in. It can be safely ignored (and left as null), or changed if you prefer.
	* maxWidth is the maximum width of the images you're using for the ghost.
	* maxHeight is the same as maxWidth, but height. 

>Anatomy of a .json Behavior File
	There are five kinds of behaviors - normal, multiline, ask, timed, and randomized. I'll go through the details and explain the components of each one, and what changing them accomplishes.

	[NORMAL]
	The default behavior type. Has the following elements:
	* "imageUsed": The name of an image file stored in the images subfolder of the resources folder. It's the image the ghost uses while performing this behavior. Change the image name, change the image used.
	* "statements": An array of text lines. Each line is a possible thing the ghost can say. You can add, remove, or change any of the lines (although there should always be at least one line in the array) to change what the ghost says when that behavior is performed. (Use /n )
	* "actTime": A floating-point number. Tells how long the behavior will be performed for, in seconds. Can go to millisecond precision, if you want to. Change it to change the time.

	[MULTILINE]
	The behavior type used for saying multiple things in a row. Has the following elements:
	* chains: A series of groups. Each group is a possible thing the ghost can say, written as three arrays (all arrays should be the same length). Each array is ordered, and each entry in each array is related to the other entries at the same "point" in their arrays. These are the arrays:
		* stmts: The sentences that the ghost will say.
		* times: How long the ghost will say the corresponding sentence for. Floating-point number, just like actTime.
		* imgStrs: The names of the images the ghost will use for the corresponding sentence. Written exactly like imageUsed. If it's null, then the image won't change from the previously used image - importantly, this does NOT mean that the first image can be null.
	To simplify: when a group gets "said," the ghost will say the first statement for the first time length with the first picture, then the second, and so on. Basically, it's a set of normal behaviors split into three parts and put next to a bunch of other normal behaviors, to be called in order. Any field can be changed, just like in the normal behavior, but all arrays in a group MUST be the same length.
	* actTime: Might as well be nonexistent, but is needed to ensure that the program doesn't throw a fit. Just leave it as -1, it affects nothing.
	* hasSetValue: A boolean value that determines whether the group selected should be internally determined by the program. If you want that to happen, you'll need to do some programming to make it so. Programmers can set it to true and override the calculate() method in your class inherited from Behavior_LongTalk to do this.

	[ASK]
	The behavior type used when the ghost asks for input. Has the following elements:
	* All the elements from the Normal behavior type. All these are identical, serve the same purpose, and can be modified the same way, although it's advised that you set actTime to a larger value so you have time to respond.
	* opts: An array of possible choices. Each choice consists of two sub-parts: line and behave. Line is the line of text the ghost lets you click on in the choice menu; behave is the behavior that will be executed if you click on that menu. Basically, if you've created a behavior and you want the ghost to do it after you answer a question from it, this is where you put the name of that behavior.
	* giveUp: The name of the behavior the ghost performs when you take too long to answer the question. If you wait too long (i.e. longer than actTime), then the ghost will perform this behavior, whatever it is.

	[TIMED]
	Actually consists of two separate .json files and classes. The first is the actual behavior, which may be any of the previous kinds of behaviors. The second is the actual timed version, which is named "Timer_[name of first].json" and which I will actually explain here. Has the following elements:
	* BehaviorExecuted: The name of the behavior that gets executed when the timer "goes off." Can be any behavior, much like the options in the Ask type. 
	* minSinceLast: *Integer* value that says how many seconds have to pass before the behavior can execute. Yes, integer - the clock for these "ticks" every second, not every fraction of a second. For every second after this, there's a 1/2 chance of the behavior executing on any given tick, providing that no other behavior is executing.
	* maxSinceLast: Also an integer value, this one tells the maximum number of seconds that can pass before the behavior WILL execute (provided that it's not executing another behavior at the moment). 

	[RANDOMIZED]
	Like the Timed behavior, it's more of a container for other behaviors, and basically executes a random behavior from its list. Has the following elements:
	* actTime: Just like in multiline, it's meaningless. Ignore it.
	* behaves: An array of behaviors. The ghost will randomly select one from this array to execute. You can include a behavior multiple times to raise the chances of it occurring; alternately, you can put "null" in the array, causing the ghost to do nothing when that possibility gets selected.
	
>How You Can Interact With Your Ghost
	The ghost is actually fairly uninteractive, because I didn't want it to be constantly getting in your way. However, there are a few things you can do with it. Most importantly:
	* By rubbing your mouse over it, you can pet it; it reacts with PetBehavior
	* By hovering your mouse over it for a few seconds without moving, it'll ask you what you want; it does this with HoverAskBehavior. You can use the information I've given you to change what and how it asks. 
	* If you download the Ghost Designer and create a timed behavior, you can have it interact with you at semi-random times of your choice (or you can staple a randomizer to that timer to make it even MORE random). 
	...and that's about it. Other functionality is still in progress, and I'd love to hear feedback if you have any ideas. I can't guarantee that I can implement them with how I've designed this, but I can try!
	
>Example of How To Change Things Simply
	Let's say I want to make the ghost complain when it gets petted. From looking at the main folder, here are my steps:
	1. Go into my resources folder
	2. Open the PetBehavior.json file
	3. Go to the statements array
	4. Put another line of text (in quotes, after a comma) in between the square brackets after statements
	5. Restart the ghost
	Done! It's really that easy!
	
>Troubleshooting and Other Notes
	I'm basically still in beta here, so there are likely to be bugs. Some things aren't bugs, though - just problems with the ghost that are built-in to the system. Here's a few of them; check these first before sending me the problem.
	
	Strings: All .JSON files should have their string values (i.e. the statements) wrapped in quotation marks. If they aren't, it'll cause problems. Also for statements, do NOT use return (i.e. the enter key). Instead, use "\n" wherever there would be a return (and yes, the direction of the slash matters). 
	Behaviors: All behavior files MUST be named [name]Behavior.json. It can be "PetBehavior.json" or "MyBehavior.json" or whatever you care to name it, but the format must be the same. Naming the file this way is what tells the ghost what to "look" for. This also means that if you want a behavior NOT to "happen" any more, you can simply remove the .json file for it, but be careful to remove all references to that behavior in other .json files.
	Other Stuff: I honestly don't know everything that can go wrong here. I know too much about my own work to test it. If you're reading this iteration of the .README file, then you're probably one of the people I've asked to beta test this, and you already know how to contact me with errors; if you're not, but you've still found something for me to fix, then thanks! Please email me at jacobmarcus42@gmail.com. 
	
>Future Plans
	There are a number of features I'm looking to add to the application, now that I've got a basic framework running. Here's a by-no-means comprehensive list; if you have a feature you really want that you don't see on here, then please leave a comment on the Github repository where you found this. 
	* Allow two ghosts to run in the same window, interacting with each other
	* Allow you to let the ghost check your email, notifying you every so often of new ones
	* Create a new behavior type for behaviors that happen sequentially
	* Allow the text box to rescale if there's too many lines/text to fit in it