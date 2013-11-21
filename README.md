The Sxy Markup Compiler
=======================
<br>
You more than welcome to join the development of this compiler :)
<br>
My email. if you have any questions or just wan't to talk to someone.<br>
sigurd@bergsvela.com

How to use it

<ol>
<li>Go to releases, and download the latest *non beta* release.</li>
<li>In the folder, you will find a SxyML.jar file, this is the only one you need, unless you want to develop Sxy further.</li>
<li>run the jar file with the argument -i *sxy file* -o *html ouput*. To do this on a Mac/Linux you would open your terminal and write <pre><code>java -jar SxyML.jar -i myfile.sxy -o myfile.html </code></pre></li>
</ol> 


<br><br>
Compiler Arguments
<pre><code>
--input-file  | -i  (path)    Spesifies the .sxy file
--output-file | -o  (path)    Spesifies where to output the parsed code. the Sxy compiler will cout the code if no output is spesified
--tab-size          (integer) Spesifies tab size
--html-style-void   (flag)    &lt;link ...> instead of &lt;link .../>
</pre>
</code>

Rules:
1. If you use this software and modify it, you are required to upload you modifications here, and share it with everybody.
2. Remember to read rule 1. :)

<br>

The syntax is simple


<pre>
<code>
@TAGNAME [attrkey:[attrvalue] ]... {
	COntent
	
	@TAGELEMENT {
		content
	}
}
</code>
</pre>
<b>
	Example:
</b>
<pre>
<code>
	@html {
		@head {
			@script src:"http://www.jquery.com/dwnload" {}
			@link type:"text/css" href:"/style.css"; //&lt;-- Semicolon to end a void element
			
			/*
			 * semi-colon end elements that are not ended like the <link> tag
			 */
			
			//Oy yeah, this is a single line comment
			/*
			 	This is a multiline comment
			 */
		}
		
		@body {
			
			@div class:"//This is NOT a comment, it inside a string!" {
				
			}
			
			@div class : "/*This is NOT a comment, it inside a string*/" {
				
			}
		
		}
	}
</code>
</pre>

SxyML does not support namespaces per say (Since it is not engorged to use them in html), but you can get the same effect
using the escape character

<pre>
<code>
@namespace\:div attr\:namespace:"value" {

}
</code>
</pre>


You can format the code prettymuch anyway you like.
This would work(But please don't do this):

<pre>
<code>
@  div attr  : "value"{
	@link src  : "value" ;
	@ script {
		function() {
			
		}
	}
}
</code>
</pre>


There is a shorthand attribute syntax which allows for setting an attribute
with the same value as key

<pre>
<code>

@select {
	@option {}
	@option {}
	@option selected {}
	//Is the same as
	@option selected:"selected" {}
}

</code>
</pre>