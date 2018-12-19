#BiTS Delegation Bot (Odoo Module)

#Installation
Installation is very straight forward. First makes sure following dependencies installed.

    pip3 install chatterbot
    pip3 install beautifulsoup4==4.4.1
    pip3 install word2number
    pip3 install html2text
    
Now installed bot module and enable bot menu from Settings>Users & Companies>Users> Other> Bits Bot and reload web page you'll see
bot menu. Now go to Bits Bot>Upload CSV, upload sample training file and click train from xml. No go Live Chat setting enable bot 
from there too and database uri and selected bot user (create new separate user recommended).



#Skill
Skill is stand for single record of training. Currently there are two types of skills available.

**Basic** : Basic skill can perform very simple static kind of task. It may trigger some action as well for instance trigger some dynamic skill, 
check user info, switch to human operator, cancel ongoing dynamic skill. It gives final immediately. Public user can trigger this type of skill.

**Dynamic** : Dynamic skill used to process odoo queries for instance create timesheet, list recent sale, assign sales lead
etc. It may perform final result immediately or force user for input missing field value first for instance create timesheet required names, unit_amount, 
project_id, etc. Public user not allowed to trigger this type of skill.

#SKill Training
There are three major component of skill training.
1. **Input Variants** - many input can used to trigger skill eg. create task, add task, add a new task will create 'project.task' record
2. **Response Arch** - Response architecture is parsable xml document used to format final result.
3. **Instruction Arch** - Instruction architecture is parsable xml document used to ask user to provide missing values. 



#Slot
Slot is very simple class which convert plain text input some kind of useful value fro instance if we need to parse proper date value then we can use 'date' slot
'date' slot can convert as follows:

     'mar 24 2018' to '2018-03-25'
     'today' to <actual date of today>


The whole concept stand for processing plan text value info valuable information. 

**date_slot** - Input format is 'may 24 2018' and Uutput format 'yyyy-MM-dd' (date formatted)

**duration_slot** - Input format is '4 hours 30 minutes' and Output format 4.50 (floating value)

**may2one_slot** - Input format is <name of record> and Output format [2,3] (list of matching record)

**number_slot** - Input format is 'one hundred twenty four' and Output format 124 (numeric value)

**selection_slot** - Input format is <any value from given list> and Output will be matched value or False in case unmatched

Please note more input format can be added followed by existing logic.


#Getting Response form bot
Call 'chatbot_response' method reside 'bot.pool' model for bot response.

    self.env['bot.pool].chatbot_response(input_html, user_id, mail_channel_id)
    

#Get skills suggestion
You can call 'best_match' method for suggested user to trigger dynamic skill
    
    #limit is optional default limit is 5 
    self.env['bot.pool].best_match(search_query,mail_channel_id,limit)

        


**Input:**

Input is simple html text which may contain various component value for instance it may contain value for specific field or some other instruction. 
Input adaptor is used for converting html text to some useful information. 

Here is simple input which only contain body:

    <input>
       <body>
           Hi there!
       </body>
    </input> 
    
*Note: 'body' tag is display text. body can be used for field value as well.*

Input with field value:

    <input>
      <body>
          Administrator
      <body>
      <value type="many2one" string="23"/>
    </input>
    
Following input pointing value 23 whose type is 'many2one'. Please note only one value tag can be passed at a time.

If we want to trigger some action we can simply use 'terminal' tag like:

    <input>
      <body>
        Create new task
      <body>
      <terminal name="launch_skill;bot_create_project_task"/>
    </input>
Following input contain 'terminal' tag along with 'name' attribute. The first part separated by semicolon
is name of action in this case it is 'launch_skill' and second one is name of skill.

More parameter for 'terminal'

    <input>
      <body>
        View more skills.
      <body>
      <terminal name="list_dynamic_skills;15;Here are some tasks I can assist you with:;Sorry I can't assist you with any system task yet." />
    </input>

name of action is 'list_dynamic_skills' and rest others is parameters.


**Output:**

Output is also html text which contain several useful tag that can help to render specif widget time in order to completed next input.

*Tags*

Following meta tag can be found on 'head'.

**chatterbot** - contain channel_id which is 'mail.channel' id and type of result.

**button** - button may trigger some terminal action. 
We should render these button on chat interface and whenever they clicked we should send input with 'terminal' tag.
Every button already contain what 'terminal' action you need to send.

**card** - card point some kind of static widget eg. link,map,video

**body** - This is simple display text. 

Here is complete example of response :

    <html>
     <head>
          <chatterbot channel_id="167" type="skill_result" />
          <button string="Create new task" terminal="launch_skill;bot_create_project_task" />
          <button string="View more skills." terminal="list_dynamic_skills;15;Here are some tasks I can assist you with:;Sorry I can't assist you with any system task yet." />
     </head>
     <body>
          <p>
           Hello! I am Administrator, your friendly BiTS BoT, how may I be of service today?
          </p>
          <p>
           Here are some tasks I can assist you with:
          </p>
     </body>
    </html>

Card Example:

       <html>
        <head>
             <chatterbot channel_id="167" type="skill_result" />
             <card source="https://busits.com" string="https://busits.com" thumbnail="http://i3.ytimg.com/vi/eHXBxxnPTK8/maxresdefault.jpg" type="video" />
        </head>
        <body>
             <p>
              BiTS helps businesses make data management simple. We deliver enterprise support and a comprehensive suite of cloud solutions. Here is a short video clip that explains more!
             </p>
        </body>
       </html>
source attr stands for url/link. We should render widget according 'type' in current case video. 

Entity Example: 
If response type is 'entity_request' then we must render some kind of widget and our upcoming input must be a valid field value.
    
    <html>
     <head>
      <chatterbot channel_id="167" type="entity_request">
      </chatterbot>
      <widget domain="[]" relation="project.project" selection="" type="many2one">
      </widget>
     </head>
     <body>
      <p>
       Please select a project for this task.
      </p>
     </body>
    </html>

Following output contain result type 'entity_request' and It is asking for 'many2one' field along with 
relation 'project.project' and empty domain. We must render some kind of searchable widget here.
And we must send next input as follow:

     <input>
           <body>
               ChatBot Web UI Development
           <body>
           <value type="many2one" string="7"/>
     </input>

This indicates the 'project.project' has been selected by user whose id is 7 and display text can be include inside body.
 

Similar Example:

    <html>
     <head>
      <chatterbot channel_id="167" type="entity_request">
      </chatterbot>
      <widget domain="[]" relation="" selection='[["this week","This week"],["this month","Current month"],["this year","Whole year"]]' type="selection">
      </widget>
     </head>
     <body>
      <p>
       Please select a duration.
      </p>
     </body>
    </html>

Now you must send next input only from given selection as follow:

        <input>
           <body>
               ChatBot Web UI Development
           <body>
           <value type="selection" string="this month"/>
        </input>
'this month' is selected value by user.




