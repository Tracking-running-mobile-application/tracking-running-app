BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "TrainingPlan" (
	"planId"	INTEGER NOT NULL,
	"planSessionId"	INTEGER,
	"title"	TEXT NOT NULL,
	"description"	TEXT NOT NULL,
	"estimatedTime"	REAL NOT NULL,
	"targetDistance"	REAL DEFAULT 0.0,
	"targetDuration"	REAL DEFAULT 0.0,
	"targetCaloriesBurned"	REAL DEFAULT 0.0,
	"goalProgress"	REAL DEFAULT 0.0,
	"difficulty"	TEXT NOT NULL,
	FOREIGN KEY("planSessionId") REFERENCES "RunSession"("sessionId"),
	PRIMARY KEY("planId" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "RunSession" (
	"sessionId"	INTEGER NOT NULL,
	"runDate"	TEXT NOT NULL,
	"distance"	REAL DEFAULT 0.0,
	"duration"	INTEGER DEFAULT 0,
	"pace"	REAL DEFAULT 0.0,
	"caloriesBurned"	REAL DEFAULT 0.0,
	"isActive"	INTEGER DEFAULT 0,
	"dateAddInFavorite"	TEXT DEFAULT NULL,
	"isFavorite"	INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY("sessionId")
);
CREATE TABLE IF NOT EXISTS "GPSPoint" (
	"gpsPointId"	INTEGER NOT NULL,
	"trackId"	INTEGER NOT NULL,
	"longitude"	REAL NOT NULL,
	"latitude"	REAL NOT NULL,
	"timeStamp"	INTEGER NOT NULL,
	FOREIGN KEY("trackId") REFERENCES "GPSTrack"("gpsTrackId") ON DELETE CASCADE,
	PRIMARY KEY("gpsPointId" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "GPSTrack" (
	"gpsTrackId"	INTEGER NOT NULL,
	"gpsSessionId"	INTEGER NOT NULL,
	"isPaused"	INTEGER NOT NULL DEFAULT 0 CHECK("isPaused" IN (0, 1)),
	PRIMARY KEY("gpsTrackId" AUTOINCREMENT),
	FOREIGN KEY("gpsSessionId") REFERENCES "RunSession"("sessionId") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "MonthlyStats" (
	"monthStatsKey"	TEXT NOT NULL,
	"totalDistance"	REAL DEFAULT 0.0,
	"totalDuration"	INTEGER DEFAULT 0,
	"totalCaloriesBurned"	REAL DEFAULT 0.0,
	"totalAvgSpeed"	REAL DEFAULT 0.0,
	PRIMARY KEY("monthStatsKey")
);
CREATE TABLE IF NOT EXISTS "Notification" (
	"notificationId"	INTEGER NOT NULL,
	"title"	TEXT NOT NULL,
	"message"	TEXT NOT NULL,
	"notificationType"	TEXT NOT NULL,
	PRIMARY KEY("notificationId" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "PersonalGoal" (
	"goalId"	INTEGER NOT NULL,
	"goalSessionId"	INTEGER,
	"name"	TEXT DEFAULT NULL,
	"targetDistance"	REAL DEFAULT 0.0,
	"targetDuration"	REAL DEFAULT 0.0,
	"targetCaloriesBurned"	REAL DEFAULT 0.0,
	"goalProgress"	REAL DEFAULT 0.0,
	"dateCreated"	TEXT NOT NULL,
	FOREIGN KEY("goalSessionId") REFERENCES "RunSession"("sessionId"),
	PRIMARY KEY("goalId" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "User" (
	"userId"	INTEGER NOT NULL,
	"name"	TEXT DEFAULT NULL,
	"age"	INTEGER DEFAULT NULL,
	"height"	REAL DEFAULT NULL,
	"weight"	REAL DEFAULT 50.0,
	"metricPreference"	TEXT DEFAULT 'km',
	"unit"	TEXT DEFAULT 'kg',
	PRIMARY KEY("userId" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "WeeklyStats" (
	"weeklyStatsKey"	TEXT NOT NULL,
	"totalDistance"	REAL DEFAULT 0.0,
	"totalDuration"	INTEGER DEFAULT 0,
	"totalCaloriesBurned"	REAL DEFAULT 0.0,
	"totalAvgSpeed"	REAL DEFAULT 0.0,
	PRIMARY KEY("weeklyStatsKey")
);
CREATE TABLE IF NOT EXISTS "YearlyStats" (
	"yearlyStatsKey"	TEXT NOT NULL,
	"totalDistance"	REAL DEFAULT 0.0,
	"totalDuration"	INTEGER DEFAULT 0,
	"totalCaloriesBurned"	REAL DEFAULT 0.0,
	"totalAvgSpeed"	REAL DEFAULT 0.0,
	PRIMARY KEY("yearlyStatsKey")
);
INSERT INTO "TrainingPlan" VALUES (1,NULL,'Running for Beginners','This plan helps new runners build a solid foundation of endurance and stamina while gradually increasing their distances.',30.0,5.0,NULL,NULL,0.0,'Beginner');
INSERT INTO "TrainingPlan" VALUES (2,NULL,'Trail Hiking Basics','Perfect for those starting their hiking journey, focusing on short distances and light trails to improve overall fitness.',60.0,2.0,NULL,NULL,0.0,'Beginner');
INSERT INTO "TrainingPlan" VALUES (3,NULL,'Mindful Movement','Combine light running with mindfulness exercises to improve focus and reduce stress while staying active.',20.0,NULL,20.0,NULL,0.0,'Beginner');
INSERT INTO "TrainingPlan" VALUES (4,NULL,'Strength Training for Trails','This plan introduces basic strength exercises to help trail runners navigate uneven terrains safely.',45.0,NULL,NULL,200.0,0.0,'Beginner');
INSERT INTO "TrainingPlan" VALUES (5,NULL,'Trail Exploration Start','Begin exploring trails with manageable distances to build your endurance and adapt to new surfaces.',70.0,10.0,NULL,NULL,0.0,'Beginner');
INSERT INTO "TrainingPlan" VALUES (6,NULL,'Easy Running Plan','Develop a consistent running habit by aligning breath and stride for better endurance and mental clarity.',30.0,NULL,30.0,NULL,0.0,'Beginner');
INSERT INTO "TrainingPlan" VALUES (7,NULL,'Hiking Preparation','Build the necessary stamina and strength for day-long hiking adventures with a focus on light trails.',90.0,3.0,NULL,NULL,0.0,'Beginner');
INSERT INTO "TrainingPlan" VALUES (8,NULL,'Introduction to Running','A beginner-friendly approach to running, focusing on consistency and gradual progression to cover moderate distances.',35.0,5.0,NULL,NULL,0.0,'Beginner');
INSERT INTO "TrainingPlan" VALUES (9,NULL,'Trail Basics','Learn the essentials of trail running, including handling varied terrains and maintaining proper balance.',50.0,6.0,NULL,NULL,0.0,'Beginner');
INSERT INTO "TrainingPlan" VALUES (10,NULL,'Mindful Jogging','Focus on syncing your breathing with movement to enhance your running efficiency and mental focus.',15.0,NULL,15.0,NULL,0.0,'Beginner');
INSERT INTO "TrainingPlan" VALUES (11,NULL,'Distance Builder','Enhance your endurance through structured runs aimed at gradually increasing the distance you can cover.',60.0,10.0,NULL,NULL,0.0,'Intermediate');
INSERT INTO "TrainingPlan" VALUES (12,NULL,'Trail Endurance Plan','Prepare for trails with significant elevation changes through targeted exercises to build stamina and strength.',75.0,NULL,NULL,400.0,0.0,'Intermediate');
INSERT INTO "TrainingPlan" VALUES (13,NULL,'Stamina for Long Runs','Focus on long, steady runs to improve endurance and prepare for covering greater distances.',90.0,15.0,NULL,NULL,0.0,'Intermediate');
INSERT INTO "TrainingPlan" VALUES (14,NULL,'Trail Fitness Plan','Challenge yourself with steady trail runs to improve your stamina and overall trail performance.',80.0,12.0,NULL,NULL,0.0,'Intermediate');
INSERT INTO "TrainingPlan" VALUES (15,NULL,'Long Distance Focus','Work on pacing and energy management for longer durations, perfect for runners aiming to push their limits.',45.0,NULL,45.0,NULL,0.0,'Intermediate');
INSERT INTO "TrainingPlan" VALUES (16,NULL,'Mindful Endurance','Practice mindful techniques while running to help you stay focused and reduce fatigue during longer runs.',30.0,NULL,30.0,NULL,0.0,'Intermediate');
INSERT INTO "TrainingPlan" VALUES (17,NULL,'Advanced Trail Exploration','Master trail techniques like handling steep inclines and sharp descents to improve trail running performance.',100.0,10.0,NULL,NULL,0.0,'Intermediate');
INSERT INTO "TrainingPlan" VALUES (18,NULL,'Trail Climbing Strength','Boost your strength with targeted hill climbing exercises designed for tackling trails with varying elevations.',90.0,NULL,NULL,450.0,0.0,'Intermediate');
INSERT INTO "TrainingPlan" VALUES (19,NULL,'Focused Running','Combine mindful exercises with your runs to build focus and reduce fatigue, enhancing your overall running experience.',40.0,NULL,40.0,NULL,0.0,'Intermediate');
INSERT INTO "TrainingPlan" VALUES (20,NULL,'Trail Endurance Challenge','Push your trail fitness with runs that test your endurance and help you adapt to challenging surfaces.',85.0,12.0,NULL,NULL,0.0,'Intermediate');
INSERT INTO "TrainingPlan" VALUES (21,NULL,'Advanced Distance Training','Prepare for advanced distances with structured long runs, speed intervals, and expert pacing strategies.',120.0,21.1,NULL,NULL,0.0,'Advanced');
INSERT INTO "TrainingPlan" VALUES (22,NULL,'Marathon Preparation','Develop the endurance needed for marathons with a plan focused on gradual distance increases and recovery.',240.0,42.2,NULL,NULL,0.0,'Advanced');
INSERT INTO "TrainingPlan" VALUES (23,NULL,'Trail Mastery','Learn advanced trail techniques to handle technical trails with steep descents, sharp turns, and rough surfaces.',150.0,20.0,NULL,NULL,0.0,'Advanced');
INSERT INTO "TrainingPlan" VALUES (24,NULL,'Endurance Running','Enhance your stamina for ultra-distance events with a mix of long runs and focused interval training.',180.0,50.0,NULL,NULL,0.0,'Advanced');
INSERT INTO "TrainingPlan" VALUES (25,NULL,'Mindful Ultra Training','Strengthen your mental focus and reduce fatigue during ultra-distance runs by practicing advanced mindfulness techniques.',90.0,NULL,90.0,NULL,0.0,'Advanced');
INSERT INTO "TrainingPlan" VALUES (26,NULL,'Trail Marathon Preparation','Train for long trail marathons with a focus on endurance, pacing, and handling technical terrains.',200.0,42.2,NULL,NULL,0.0,'Advanced');
INSERT INTO "TrainingPlan" VALUES (27,NULL,'Elite Hiking Endurance','Prepare for multi-day hiking adventures with targeted exercises to build strength and long-lasting endurance.',240.0,20.0,NULL,NULL,0.0,'Advanced');
INSERT INTO "TrainingPlan" VALUES (28,NULL,'Marathon Excellence','Focus on advanced strategies and training to achieve peak performance in competitive marathon events.',240.0,42.2,NULL,NULL,0.0,'Advanced');
INSERT INTO "TrainingPlan" VALUES (29,NULL,'Trail Climbing Mastery','Master steep climbs and descents with advanced training techniques to boost power and adaptability.',180.0,30.0,NULL,NULL,0.0,'Advanced');
INSERT INTO "TrainingPlan" VALUES (30,NULL,'Ultra Running Focus','Develop both the physical and mental endurance needed for ultra-distance running events with this advanced training.',300.0,NULL,120.0,NULL,0.0,'Advanced');
INSERT INTO "Notification" VALUES (1,'Halfway There!','You are halfway through your goal. Keep going!','HALF');
INSERT INTO "Notification" VALUES (2,'Keep Pushing!','Halfway to your goal. Stay strong!','HALF');
INSERT INTO "Notification" VALUES (3,'Midway Success!','You’re halfway done. Finish strong!','HALF');
INSERT INTO "Notification" VALUES (4,'Great Progress!','Half the journey is complete. Don’t stop now!','HALF');
INSERT INTO "Notification" VALUES (5,'Almost There!','Halfway point reached. Keep up the momentum!','HALF');
INSERT INTO "Notification" VALUES (6,'Halfway Hero!','You’re halfway there. Great job so far!','HALF');
INSERT INTO "Notification" VALUES (7,'50% Complete!','You’ve completed half your goal. Push on!','HALF');
INSERT INTO "Notification" VALUES (8,'Midway Milestone!','Halfway through. Stay focused!','HALF');
INSERT INTO "Notification" VALUES (9,'Halfway Achieved!','You’ve hit the halfway mark. Keep moving!','HALF');
INSERT INTO "Notification" VALUES (10,'Halfway Done!','Only halfway left to go. You’ve got this!','HALF');
INSERT INTO "Notification" VALUES (11,'Goal Achieved!','Congratulations! You’ve completed your goal!','COMPLETE');
INSERT INTO "Notification" VALUES (12,'Mission Complete!','Well done! You’ve reached your target!','COMPLETE');
INSERT INTO "Notification" VALUES (13,'Outstanding!','You’ve accomplished your goal. Amazing work!','COMPLETE');
INSERT INTO "Notification" VALUES (14,'Success!','You’ve successfully completed your goal!','COMPLETE');
INSERT INTO "Notification" VALUES (15,'Complete Triumph!','Goal achieved. Time to celebrate!','COMPLETE');
INSERT INTO "Notification" VALUES (16,'Victory!','You’ve done it. Goal complete!','COMPLETE');
INSERT INTO "Notification" VALUES (17,'Achievement Unlocked!','Congratulations on completing your goal!','COMPLETE');
INSERT INTO "Notification" VALUES (18,'Well Done!','You’ve completed your goal. Great job!','COMPLETE');
INSERT INTO "Notification" VALUES (19,'You Did It!','Goal achieved. Fantastic effort!','COMPLETE');
INSERT INTO "Notification" VALUES (20,'Target Reached!','Congratulations! You’ve hit your goal!','COMPLETE');
INSERT INTO "Notification" VALUES (21,'Take a Break!','You’ve earned a break. Rest up!','BREAK');
INSERT INTO "Notification" VALUES (22,'Pause and Relax!','Take some time to recharge.','BREAK');
INSERT INTO "Notification" VALUES (23,'Break Time!','Relax and refresh. You deserve it.','BREAK');
INSERT INTO "Notification" VALUES (24,'Step Back!','Take a short break. You’ve been working hard.','BREAK');
INSERT INTO "Notification" VALUES (25,'Rest Up!','Time for a break. Recharge your energy.','BREAK');
INSERT INTO "Notification" VALUES (26,'Slow Down!','Pause and recover. Great effort so far.','BREAK');
INSERT INTO "Notification" VALUES (27,'Recharge!','Take a moment to rest and rejuvenate.','BREAK');
INSERT INTO "Notification" VALUES (28,'Breathe!','Catch your breath. It’s break time.','BREAK');
INSERT INTO "Notification" VALUES (29,'Refresh!','Time to relax. You’ve earned it.','BREAK');
INSERT INTO "Notification" VALUES (30,'Recover!','Take a well-deserved break and get ready to go again.','BREAK');
INSERT INTO "Notification" VALUES (31,'Caution!','Your speed is unusually high! Proceed at your own risk!','EXCESSIVE_SPEED');
INSERT INTO "Notification" VALUES (32,'Enable Internet for Better Tracking', 'For accurate location tracking and route drawing, please turn on your internet.', 'NO_NETWORK');
INSERT INTO "Notification" VALUES (33,'Internet Needed for Route Map', 'Ensure your internet is enabled to get the best route drawing experience for your running sessions.', 'NO_NETWORK');

CREATE INDEX IF NOT EXISTS "index_GPSPoint_trackId" ON "GPSPoint" (
	"trackId"
);
CREATE INDEX IF NOT EXISTS "index_GPSTrack_gpsSessionId" ON "GPSTrack" (
	"gpsSessionId"
);
CREATE INDEX IF NOT EXISTS "index_PersonalGoal_goalSessionId" ON "PersonalGoal" (
	"goalSessionId"
);
COMMIT;
