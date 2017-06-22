#Final project for   Big Data Analytics 2017

<b>P8</b> Publication tone (author confidence) detection. By tone we want to see if research results are described by the authors as <i>outstanding</i>, <i>average</i>, or <i>“good-enough”</i>.

    Application:
    Infer publication contribution based on confidence-level for the results described in the paper. This is useful information when correlating scientific impact (e.g. via bibliometric metrics such as times cited) and author self-confidence.

 <h2> What is inside: </h2>
 <ul>
    <li>
        <p>To run the project first we have to build the jar. For that run <i>maven package</i>, this will create 2 jar files in target file.</p>
    </li>
    <li>
        <p>Pig file that process the initial xml files and retrives from them title of publication, the content of the publication etc. To run use</p>
            pig -x mapreduce [path to] extractData.pig
           
            
    To see the output open src/main/resources/pmc_oa_processed_pig_example
   </li>
   <li>
        <p>The map reduce job in <i>src/main/java</i> :</p>
        <ul>
            <li>
                <p>Main class: <b>AnlSemanticText</b>, start the mapreduce task.</p>
            </li>
            <li>
                <p>Mapper class: <b>SemanticMapper</b>, will read the files produced by pig and read 1 line at a time 
                clear the input of non-words and make it lower case. Will output a key (holds the name of the publication, the owner etc.) and a value (holds the sentiment score).</p>
            </li>
            <li>
                <p>Reducer class: <b>AnlSemanticText</b>, simple reducer just takes the value and push it to the output.</p>
            </li>
            <li>
                <p><b>SentiWordNet</b> holds a simple implementation of the SentiWordNet library, for each word it will check the score for each part of speach for that word. If that word
                is not in the dictionary then a 0 (has no influence on the word score) will be return</p>
            </li>
            <li>
                <p><b>FileProcessor</b> will take the output of map-reduce job and merge the data into JSON files. For that download the map-reduce results file from hadoop and add the path of the files in <b>FINAL_FINAL</b> file <b>Test.java</b> line <b>18</b>.</p>
            </li>
        </ul>
        
        To run the map-reduce job use hadoop jar MapReduce-1.0-SNAPSHOT.jar AnlSemanticText [pig_processed_files] [output file].
        To see the full output of the map-reduce job look at src/main/resources/pmc_oa_processed_mapreduce.
   </li>
   <li>
        <p>Data visualization can be seen by opening: <i>d3-visualization.html</i>. To change the data input, in the file line <b>104</b> change the path to a new file.</p>
   </li>
   <li><p>Prezentarea poate fii gasit la: <i>https://slides.com/stefanhagiu/hadoop</i> </p></li>
 </ul># BDA
