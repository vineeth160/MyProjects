//Read CSV and create groups and add users to groups and sites

var siteName = 'demo-new-site';
var logLocation = companyhome.childByNamePath('Test Location');
var logfile = logLocation.childByNamePath('demo-site_user_groups.csv');

var site = siteService.getSite(siteName);


if (site) {
    var lines = logfile.content.split('\n');

    for (var i = 1; i < lines.length; i++) {

        var grpArry = lines[i].split(',');

        var grpFullName = grpArry[1];
        var grpDisplayName = grpArry[2];
        var grpShortName = grpArry[3];
        var grpRole = grpArry[4];
        var users = grpArry[5];
       
       //creates new grp
        if (grpFullName != 'NA' && grpDisplayName != 'NA' && grpShortName != 'NA') {
            var groupData = groups.getGroup(grpShortName);
 
            if (groupData == null) {
                logger.log("Group '" + grpFullName + "' doesn't exist , creating the group");
                var newGroup = groups.createRootGroup(grpShortName, grpDisplayName);
                addGroupUsers(users, newGroup.fullName);
                var authorityName = newGroup.fullName;
                site.setMembership(authorityName, grpRole);
                logger.log("Group '"+newGroup.fullName+"' added to site '"+site.getShortName()+"' with role '"+grpRole);

          //add users to the existing grp
            } else {
                logger.log("Group '" + groupData.fullName + "' already exist");
                addGroupUsers(users, groupData.fullName)
                site.setMembership(groupData.fullName, grpRole);
                logger.log("Group '"+groupData.fullName+"' added to site '"+site.getShortName()+"' with role '"+grpRole);
            }

            site.save();

        } else if (grpFullName == 'NA' && grpDisplayName == 'NA' && grpShortName == 'NA') {
            if (users.indexOf('|') == -1) {
            	var user = people.getPerson(users.trim());
            	if(user){
            		logger.log("Person '"+users+"' already exist");
            		site.setMembership(users.trim(), grpRole);	
            		logger.log("Person '"+users+"' added to site '"+site.getShortName()+"' with role '"+grpRole);
            	}
            }
        }
    }
}

//add grp users method
function addGroupUsers(users, groupfullName) {
    if (users.indexOf('|') != -1) {
        var usersArry = users.trim().split('\\|');
        var group = people.getGroup(groupfullName);
        if (group) {
            for (x in usersArry) {

                var gUser = people.getPerson(usersArry[x].trim());
                if (gUser) {
                    people.addAuthority(group, gUser);
                    logger.log("person '" + gUser.properties["userName"] + "' added to the group " + groupfullName);
                }
            }
        }
    }

}