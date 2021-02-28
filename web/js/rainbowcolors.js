
function updateText()
{
    var oldString = document.getElementById("banner").innerHTML;

    var newString = "";
    for (var i = 0; i < oldString.length; i++)
    {
        if(oldString[i] == " ")
        {
            newString = newString + " ";
        }
        else
        {
            newString = newString + "<span>" + oldString[i] + "</span>";
        }
    }

    document.getElementById("banner").innerHTML = newString;
}