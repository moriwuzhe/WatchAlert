package models

import (
	"time"
)

type UserRole struct {
	ID          string            `json:"id" gorm:"primaryKey;type:varchar(255)"`
	RoleID      string            `json:"role_id" gorm:"column:role_id;type:varchar(255);not null"`
	UserID      string            `json:"user_id" gorm:"column:user_id;type:varchar(255);not null"`
	CreatedAt   time.Time         `json:"created_at" gorm:"column:created_at"`
	UpdatedAt   time.Time         `json:"updated_at" gorm:"column:updated_at"`
	Name        string            `json:"name" gorm:"column:name;type:longtext"`
	Description string            `json:"description" gorm:"column:description;type:longtext"`
	Permissions []UserPermissions `json:"permissions" gorm:"column:permissions;type:longtext;serializer:json"`
	CreateAt    int64             `json:"create_at" gorm:"column:create_at"`
}

func (ur *UserRole) TableName() string {
	return "user_roles"
}

type UserRoleQuery struct {
	ID          string `json:"id" form:"id"`
	Name        string `json:"name" form:"name"`
	Description string `json:"description" form:"description"`
}
