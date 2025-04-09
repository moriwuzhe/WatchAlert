package models

type Member struct {
	UserId     string   `json:"userid" gorm:"user_id;type:VARCHAR(255)"`
	UserName   string   `json:"username" gorm:"user_name;type:VARCHAR(255)"`
	Email      string   `json:"email"`
	Phone      string   `json:"phone"`
	Password   string   `json:"password"`
	Role       string   `json:"role"`
	CreateBy   string   `json:"create_by"`
	CreateAt   int64    `json:"create_at"`
	JoinDuty   string   `json:"joinDuty" gorm:"join_duty;type:VARCHAR(255)"`
	DutyUserId string   `json:"dutyUserId" gorm:"duty_user_id;type:VARCHAR(255)"`
	Tenants    []string `json:"tenants" gorm:"tenants;serializer:json"`
}

type MemberQuery struct {
	UserId   string `json:"userid" form:"userid"`
	UserName string `json:"username" form:"username"`
	Email    string `json:"email" form:"email"`
	Phone    string `json:"phone" form:"phone"`
	Query    string `json:"query" form:"query"`
	JoinDuty string `json:"joinDuty" form:"joinDuty"`
}
