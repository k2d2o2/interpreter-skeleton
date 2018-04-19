
## Intellij Settings
 * Install Antlr4 plugin
 * Run "./sbt compile" or "sbt.bat - compile" or "compile" using sbt shell
 * Unmark source target/scala-2.12/src_managed/main/antlr4 in File - Project Structure - Modules if you want to build project with intellij
   * you should do this job every time you refresh sbt in Intellij
 * PA1 file setting
   * File - Settings - Editor - File Types
     * add file types
       * Add keywords: := ; def else if print return while
       * comment: //
       * check paired parens, paired braces
     * add patterns: *.pa1
## Target Language
### File extension
* *.pa1
### Syntax
  *program* -> *function** main-*function*<br/>
  *function* -> **def** *name* *params* **:=** *block*<br/>
  *name* -> string<br/>
  *params* -> **(** variable* **)**<br/>
  *block* -> **{** *stmt** **}** | *stmt*<br/>
  *stmt* -> *lv*:=*e* **;**<br/>
  &nbsp;&nbsp;&nbsp;&nbsp;| **if** **(** *e* **)** *block* **else** *block*<br/>
  &nbsp;&nbsp;&nbsp;&nbsp;| **if** **(** *e* **)** *block* <br/>
  &nbsp;&nbsp;&nbsp;&nbsp;| **while** **(** *e* **)** *block*<br/>
  &nbsp;&nbsp;&nbsp;&nbsp;| **return** *e* **;**<br/>
  &nbsp;&nbsp;&nbsp;&nbsp;| **print** *e* **;**<br/>
  &nbsp;&nbsp;&nbsp;&nbsp;| **readline** *lv* **;**<br/>
  *e* -> **(** e **)** | *arith* | **!** *e* | e **==** e | e **!=** e | *arith* **>** *arith* | *e* **&&** *e* | e **||** e | **true** | **false** <br/>
  *arith* -> **-** *arith* | *arith* **+** *arith* | *arith* **-** *arith* |  *arith* **\*** *arith* |  *arith* **/** *arith*  | integer | *lv* | f **(** *lv* **)** <br/>
  *lv* -> variable<br/>

### AST
  * See com.dev.pa.AST

### Semantics
  * intuitive

## Goal
  * Complete the PA1 interpreter code by implementing **???** s
  * This tutorial may help those who are not familiar with Scala.
    * <https://www.scala-exercises.org/scala_tutorial/terms_and_types>
