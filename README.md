A kinda no-purpos markup language that compiles to html.

The syntax is simple


<pre>
<code>
@TAGNAME [attrkey:attrvalue ]... {
	COntent
	
	@TAGELEMENT {
		content
	}
}
<code>
<pre>
<b>
	Example:
</b>
<pre>
<code>
	@html {
		@head {
			@script src:"http://www.jquery.com/dwnload" {}
			@link type:"text/css" href:"/style.css"; //<-- Semicolon to end a void element
			
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
