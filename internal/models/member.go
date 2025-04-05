package models

type Member struct {
	UserId     string   `json:"userid" gorm:"primaryKey;type:varchar(255)"`
	UserName   string   `json:"username" gorm:"type:varchar(255)"`
	Email      string   `json:"email" gorm:"type:varchar(255)"`
	Phone      string   `json:"phone" gorm:"type:varchar(255)"`
	Password   string   `json:"password" gorm:"type:varchar(255)"`
	Role       string   `json:"role" gorm:"type:varchar(255)"`
	CreateBy   string   `json:"create_by" gorm:"type:varchar(255)"`
	CreateAt   int64    `json:"create_at"`
	JoinDuty   string   `json:"joinDuty" gorm:"type:varchar(255)"`
	DutyUserId string   `json:"dutyUserId" gorm:"type:varchar(255)"`
	Tenants    []string `json:"tenants" gorm:"tenants;serializer:json"`
}

func (m *Member) TableName() string {
	return "members"
}

type MemberQuery struct {
	UserId   string `json:"userid" form:"userid"`
	UserName string `json:"username" form:"username"`
	Email    string `json:"email" form:"email"`
	Phone    string `json:"phone" form:"phone"`
	Query    string `json:"query" form:"query"`
	JoinDuty string `json:"joinDuty" form:"joinDuty"`
}
