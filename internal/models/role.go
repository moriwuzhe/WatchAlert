package models

import "time"

type Role struct {
	ID          string    `json:"id" gorm:"primaryKey;type:varchar(255)"`
	Name        string    `json:"name" gorm:"unique;not null;type:varchar(255)"`
	Description string    `json:"description" gorm:"type:varchar(255)"`
	CreatedAt   time.Time `json:"created_at" gorm:"column:created_at"`
	UpdatedAt   time.Time `json:"updated_at" gorm:"column:updated_at"`
}

func (r *Role) TableName() string {
	return "roles"
}

type RoleQuery struct {
	ID          string `json:"id" form:"id"`
	Name        string `json:"name" form:"name"`
	Description string `json:"description" form:"description"`
}
